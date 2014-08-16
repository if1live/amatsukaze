package net.yzwlab.javammd.format;

import net.yzwlab.javammd.IReadBuffer;
import net.yzwlab.javammd.ReadException;

public class BasicReader {
	/**
	 *  함수 인자로 대입할 대상을 넣으면 오버로딩이 가능해서 함수가 깔끔해진다
	 */
	
	public static MMD_COLOR3 read(IReadBuffer buffer, MMD_COLOR3 val) throws ReadException {
		assert buffer != null;
		val.r = (byte)(buffer.readFloat() * 255);
		val.g = (byte)(buffer.readFloat() * 255);
		val.b = (byte)(buffer.readFloat() * 255);
		return val;
	}
	
	public static MMD_COLOR4 read(IReadBuffer buffer, MMD_COLOR4 val) throws ReadException {
		assert buffer != null;
		val.r = (byte)(buffer.readFloat() * 255);
		val.g = (byte)(buffer.readFloat() * 255);
		val.b = (byte)(buffer.readFloat() * 255);
		val.a = (byte)(buffer.readFloat() * 255);
		return val;
	}
	
	public static MMD_VECTOR2 read(IReadBuffer buffer, MMD_VECTOR2 val) throws ReadException {
		assert buffer != null;
		val.x = buffer.readFloat();
		val.y = buffer.readFloat();
		return val;
	}
	
	public static MMD_VECTOR3 read(IReadBuffer buffer, MMD_VECTOR3 val) throws ReadException {
		assert buffer != null;
		val.x = buffer.readFloat();
		val.y = buffer.readFloat();
		val.z = buffer.readFloat();
		return val;
	}
	
	public static MMD_VECTOR4 read(IReadBuffer buffer, MMD_VECTOR4 val) throws ReadException {
		assert buffer != null;
		val.x = buffer.readFloat();
		val.y = buffer.readFloat();
		val.z = buffer.readFloat();
		val.w = buffer.readFloat();
		return val;
	}
	
	public static MMD_MATRIX read(IReadBuffer buffer, MMD_MATRIX val) throws ReadException {
		assert buffer != null;
		val.values = buffer.readFloatArray(4, 4);
		return val;
	}
	
	public static MMD_VERTEX_TEXUSE read(IReadBuffer buffer, MMD_VERTEX_TEXUSE val) throws ReadException {
		assert buffer != null;
		val.point.copyFrom((new MMD_VECTOR3()).read(buffer));
		val.normal.copyFrom((new MMD_VECTOR3()).read(buffer));
		val.uv.copyFrom((new MMD_VECTOR2()).read(buffer));
		return val;
	}
	
	public static MMD_MOTION_PAD read(IReadBuffer buffer, MMD_MOTION_PAD val) throws ReadException {
		assert buffer != null;
		val.cInterpolationX = buffer.readByteArray(16);
		val.cInterpolationY = buffer.readByteArray(16);
		val.cInterpolationZ = buffer.readByteArray(16);
		val.cInterpolationRot = buffer.readByteArray(16);
		return val;
	}
	
	///////////////////////////
	
	public static MMD_COLOR3 read_MMD_COLOR3(IReadBuffer buffer) throws ReadException {
		return read(buffer, new MMD_COLOR3());
	}
	public static MMD_COLOR4 read_MMD_COLOR4(IReadBuffer buffer) throws ReadException {
		return read(buffer, new MMD_COLOR4());
	}
	public static MMD_VECTOR2 read_MMD_VECTOR2(IReadBuffer buffer) throws ReadException {
		return read(buffer, new MMD_VECTOR2());
	}
	public static MMD_VECTOR3 read_MMD_VECTOR3(IReadBuffer buffer) throws ReadException {
		return read(buffer, new MMD_VECTOR3());
	}
	public static MMD_VECTOR4 read_MMD_VECTOR4(IReadBuffer buffer) throws ReadException {
		return read(buffer, new MMD_VECTOR4());
	}
	
	public static MMD_VERTEX_TEXUSE read_MMD_VERTEX_TEXUSE(IReadBuffer buffer) throws ReadException {
		return read(buffer, new MMD_VERTEX_TEXUSE());
	}
	
	public static MMD_MOTION_PAD read_MMD_MOTION_PAD(IReadBuffer buffer) throws ReadException {
		return read(buffer, new MMD_MOTION_PAD());
	}
	
	public static MMD_MATRIX read(IReadBuffer buffer) throws ReadException {
		return read(buffer, new MMD_MATRIX());
	}
}
