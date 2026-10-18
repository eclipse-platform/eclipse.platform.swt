/*
 * Copyright 2026 Hitesh Soliwal and contributors
 * SPDX-License-Identifier: EPL-2.0 OR Apache-2.0
 *
 * Dual-licensed by the rights holder's decision of 2026-10-13: you may use this file under the
 * Eclipse Public License 2.0 (https://www.eclipse.org/legal/epl-2.0/) or the Apache License 2.0
 * (https://www.apache.org/licenses/LICENSE-2.0).
 */
package org.eclipse.swt.internal.swap;

import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.file.*;
import java.util.*;

import org.eclipse.swt.internal.PackedHeapGrowth;

/**
 * Java {@link SwapStore} for desktop SWT: the fail-closed backend when the Synexia JNI SQLite
 * store is absent (SWT ships no native swap library). Session-scoped: one delete-on-close
 * temporary file written with positional {@link FileChannel} I/O; the OS page cache is the
 * second-level cache. Record bytes move through {@link SwapPageBinaryOps#copy} (Java ops, or a
 * native candidate under Java parity checking, see {@link SwapPageBinaryOpsSelector}).
 *
 * <p>Ids come from {@link #reserve(int)}: {@code (sequence << 32) | (block << 5) | class}, so a
 * record's location is in its id and no per-record heap index exists. Space is recycled through
 * power-of-two block classes (32-byte blocks) on int stacks (MapDB {@code StoreDirect} free-list
 * pattern); the sequence makes every id unique, so a recycled block never revives a stale id.
 * Each region starts with a 16-byte header: id (8), length (4), class (4). Records are immutable;
 * an exact duplicate write is idempotent and conflicting bytes fail before any mutation.</p>
 */
public final class FileSwapStore implements SwapStore {
	static final int BLOCK_SHIFT = 5, CLASS_BITS = 5, HEADER = 16, MAX_BLOCKS = 1 << (31 - CLASS_BITS);

	private final FileChannel channel;
	private final SwapPageBinaryOps ops;
	private final int [][] free = new int [27][];
	private final int [] freeCount = new int [27];
	private final Map<String, Head> heads = new HashMap<> ();
	private final ByteBuffer header = ByteBuffer.allocate (HEADER);
	private ByteBuffer scratch = ByteBuffer.allocateDirect (4096);
	private int nextBlock, sequence;
	private long live, fileBytes, reused;

	public FileSwapStore (Path file, SwapPageBinaryOps ops) throws IOException {
		this.ops = Objects.requireNonNull (ops, "ops");
		channel = FileChannel.open (file, StandardOpenOption.CREATE_NEW, StandardOpenOption.READ,
				StandardOpenOption.WRITE, StandardOpenOption.DELETE_ON_CLOSE);
	}

	/** A store in {@code java.io.tmpdir} using the selected page ops; deleted on close. */
	public static FileSwapStore temporary () throws IOException {
		Path dir = Paths.get (System.getProperty ("java.io.tmpdir"));
		return new FileSwapStore (dir.resolve ("swt-swap-" + ProcessHandle.current ().pid () + "-" + System.nanoTime () + ".swap"),
				SwapPageBinaryOpsSelector.select (null));
	}

	private static int classOf (int bytes) {
		int blocks = (bytes + (1 << BLOCK_SHIFT) - 1) >>> BLOCK_SHIFT;
		return 32 - Integer.numberOfLeadingZeros (Math.max (1, blocks) - 1);
	}

	private static long offset (long id) {
		return (long) (((int) id) >>> CLASS_BITS) << BLOCK_SHIFT;
	}

	private static int capacityOf (long id) {
		return (1 << (((int) id) & ((1 << CLASS_BITS) - 1)) << BLOCK_SHIFT) - HEADER;
	}

	/** Allocates space for a record of {@code length} bytes and returns its unique id. */
	public synchronized long reserve (int length) throws IOException {
		if (length < 0 || length > maxRecordBytes ()) throw new IOException ("record length " + length);
		int cls = classOf (length + HEADER);
		int block;
		if (freeCount [cls] > 0) {
			block = free [cls] [--freeCount [cls]];
			reused++;
		} else {
			if (nextBlock > MAX_BLOCKS - (1 << cls)) throw new IOException ("swap file full (2 GiB)");
			block = nextBlock;
			nextBlock += 1 << cls;
		}
		sequence = sequence == Integer.MAX_VALUE ? 1 : sequence + 1;
		return ((long) sequence << 32) | ((long) block << CLASS_BITS) | cls;
	}

	/** Returns a record's space; its id reads as absent afterwards. */
	public synchronized void release (long id) throws IOException {
		SwapStore.requireId (id);
		if (!matches (id)) return;
		int cls = ((int) id) & ((1 << CLASS_BITS) - 1);
		writeHeader (offset (id), 0L, 0, cls);
		int [] stack = free [cls];
		if (stack == null) stack = free [cls] = new int [16];
		if (freeCount [cls] == stack.length) stack = free [cls] = Arrays.copyOf (stack, PackedHeapGrowth.capacity (stack.length, stack.length + 1, 4));
		stack [freeCount [cls]++] = ((int) id) >>> CLASS_BITS;
		live--;
	}

	private boolean matches (long id) throws IOException {
		if (offset (id) >= (long) nextBlock << BLOCK_SHIFT) return false;
		readHeader (offset (id));
		return header.getLong (0) == id;
	}

	private void readHeader (long at) throws IOException {
		header.clear ();
		while (header.hasRemaining ()) {
			if (channel.read (header, at + header.position ()) < 0) {
				header.putLong (0, 0L);
				return;
			}
		}
	}

	private void writeHeader (long at, long id, int length, int cls) throws IOException {
		header.clear ();
		header.putLong (0, id).putInt (8, length).putInt (12, cls);
		while (header.hasRemaining ()) {
			channel.write (header, at + header.position ());
		}
	}

	@Override
	public synchronized byte [] read (long id) throws IOException {
		SwapStore.requireId (id);
		if (!matches (id)) return null;
		int length = header.getInt (8);
		ByteBuffer direct = scratch (length);
		while (direct.hasRemaining ()) {
			if (channel.read (direct, offset (id) + HEADER + direct.position ()) < 0) throw new EOFException ("swap record truncated");
		}
		direct.flip ();
		byte [] out = new byte [length];
		ops.copy (direct, ByteBuffer.wrap (out));
		return out;
	}

	private ByteBuffer scratch (int length) {
		if (scratch.capacity () < length) scratch = ByteBuffer.allocateDirect (PackedHeapGrowth.capacity (scratch.capacity (), length, 1));
		scratch.clear ().limit (length);
		return scratch;
	}

	@Override
	public synchronized void write (Map<Long, byte []> records, Publication publication) throws IOException {
		Objects.requireNonNull (records, "records");
		if (records.size () > maxBatchRecords ()) throw new IOException ("batch too large");
		List<Long> fresh = new ArrayList<> ();
		for (Map.Entry<Long, byte []> e : records.entrySet ()) {
			long id = e.getKey ();
			SwapStore.requireId (id);
			byte [] bytes = Objects.requireNonNull (e.getValue (), "record bytes");
			if (bytes.length > capacityOf (id)) throw new IOException ("record " + id + " exceeds its reservation");
			byte [] existing = read (id);
			if (existing == null) fresh.add (id);
			else if (!Arrays.equals (existing, bytes)) throw new IOException ("conflicting bytes for record " + id);
		}
		if (publication != null) checkPublication (publication);
		for (long id : fresh) {
			byte [] bytes = records.get (id);
			ByteBuffer direct = scratch (bytes.length);
			ops.copy (ByteBuffer.wrap (bytes), direct);
			direct.limit (bytes.length).position (0);
			long at = offset (id) + HEADER;
			while (direct.hasRemaining ()) {
				channel.write (direct, at + direct.position ());
			}
			writeHeader (offset (id), id, bytes.length, ((int) id) & ((1 << CLASS_BITS) - 1));
			fileBytes = Math.max (fileBytes, at + bytes.length);
			live++;
		}
		if (publication != null) {
			Head current = head (publication.name ());
			heads.put (publication.name (), new Head (current.revision () + 1, publication.recordId ()));
		}
	}

	private void checkPublication (Publication p) throws IOException {
		if (head (p.name ()).revision () != p.expectedRevision ()) throw new IOException ("stale publication " + p.name ());
	}

	@Override
	public synchronized Head head (String name) {
		Head h = heads.get (SwapStore.requireName (name));
		return h == null ? new Head (0, -1) : h;
	}

	@Override
	public synchronized long [] ids (long afterExclusive, int limit) throws IOException {
		long [] all = new long [16];
		int n = 0;
		for (int block = 0; block < nextBlock;) {
			readHeader ((long) block << BLOCK_SHIFT);
			long id = header.getLong (0);
			int cls = header.getInt (12);
			if (id > afterExclusive && ((int) id) >>> CLASS_BITS == block) {
				if (n == all.length) all = Arrays.copyOf (all, n * 2);
				all [n++] = id;
			}
			block += 1 << Math.max (0, Math.min (cls, 26));
		}
		Arrays.sort (all, 0, n);
		return Arrays.copyOf (all, Math.min (n, Math.max (0, limit)));
	}

	@Override
	public synchronized long count () {
		return live;
	}

	/** Highest file offset written (bytes). */
	public synchronized long fileBytes () {
		return fileBytes;
	}

	public synchronized long reusedRegions () {
		return reused;
	}

	public String opsId () {
		return ops.id ();
	}

	@Override
	public int maxRecordBytes () {
		return (1 << 26) - HEADER;
	}

	@Override
	public int maxBatchBytes () {
		return 1 << 28;
	}

	@Override
	public int maxBatchRecords () {
		return 1 << 16;
	}

	@Override
	public synchronized void close () throws IOException {
		channel.close ();
	}
}
