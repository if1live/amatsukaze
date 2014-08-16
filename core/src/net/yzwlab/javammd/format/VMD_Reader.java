package net.yzwlab.javammd.format;

import net.yzwlab.javammd.IReadBuffer;
import net.yzwlab.javammd.ReadException;

public class VMD_Reader extends BasicReader {
	public static VMD_V2_HEADER read(IReadBuffer buffer, VMD_V2_HEADER val) throws ReadException {
		val.actor = buffer.readByteArray(10);
		return val;
	}
	
	public static VMD_V3_HEADER read(IReadBuffer buffer, VMD_V3_HEADER val) throws ReadException {
		val.actor = buffer.readByteArray(20);
		return val;
	}

	public static VMD_MOTION_RECORD read(IReadBuffer buffer, VMD_MOTION_RECORD val) throws ReadException {
		val.name = buffer.readByteArray(15);
		val.frame_no = buffer.readInteger();
		val.pos = buffer.readFloatArray(3);
		val.qt = buffer.readFloatArray(4);
		val.pad = buffer.readByteArray(0x40);
		return val;
	}
	
	public static VMD_MORP_RECORD read(IReadBuffer buffer, VMD_MORP_RECORD val) throws ReadException {
		val.name = buffer.readByteArray(15);
		val.frame_no = buffer.readInteger();
		val.factor = buffer.readFloat();
		return val;
	}
	public static VMD_LIGHT_RECORD read(IReadBuffer buffer, VMD_LIGHT_RECORD val) throws ReadException {
		val.frame_no = buffer.readInteger();
		val.pos = buffer.readFloatArray(3);
		val.direction = buffer.readFloatArray(3);
		return val;
	}

	public static VMD_HEADER read(IReadBuffer buffer, VMD_HEADER val) throws ReadException {
		val.hdr_string = buffer.readByteArray(0x1A);
		val.unknown = buffer.readByteArray(0x04);
		return val;
	}

	public static VMD_CAMERA_RECORD read(IReadBuffer buffer, VMD_CAMERA_RECORD val) throws ReadException {
		val.frame_no = buffer.readInteger();
		val.pos = buffer.readFloatArray(4);
		val.angle = buffer.readFloatArray(3);
		val.pad = buffer.readByteArray(29);
		return val;
	}
}
