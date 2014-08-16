package net.yzwlab.javammd.format;

import net.yzwlab.javammd.IReadBuffer;
import net.yzwlab.javammd.ReadException;

public class PMD_Reader extends BasicReader {
	public static PMD_HEADER read(IReadBuffer buffer, PMD_HEADER val) throws ReadException {
		assert buffer != null;
		val.header1 = buffer.readByteArray(PMD_HEADER.HEADER_1_SIZE);
		val.header2 = buffer.readByteArray(PMD_HEADER.HEADER_2_SIZE);
		return val;
	}
	
	public static PMD_INDEX_RECORD read(IReadBuffer buffer, PMD_INDEX_RECORD val) throws ReadException {
		assert buffer != null;
		val.id = buffer.readShort();
		return val;
	}
	
	public static PMD_VERTEX_RECORD read(IReadBuffer buffer, PMD_VERTEX_RECORD val) throws ReadException {
		assert buffer != null;
		val.x = buffer.readFloat();
		val.y = buffer.readFloat();
		val.z = buffer.readFloat();
		val.nx = buffer.readFloat();
		val.ny = buffer.readFloat();
		val.nz = buffer.readFloat();
		val.tx = buffer.readFloat();
		val.ty = buffer.readFloat();
		val.b1 = buffer.readShort();
		val.b2 = buffer.readShort();
		val.bw = buffer.readByte();
		val.unknown = buffer.readByte();
		return val;
	}
	
	public static PMD_GRP_RECORD read(IReadBuffer buffer, PMD_GRP_RECORD val) throws ReadException {
		assert buffer != null;
		val.BoneNo = buffer.readShort();
		val.grp = buffer.readByte();
		return val;
	}
	
	public static PMD_MORP_VERTEX_RECORD read(IReadBuffer buffer, PMD_MORP_VERTEX_RECORD val) throws ReadException {
		assert buffer != null;
		val.no = buffer.readInteger();
		val.vec = buffer.readFloatArray(3);
		return val;
	}
	
	public static PMD_RIGID_BODY_RECORD read(IReadBuffer buffer, PMD_RIGID_BODY_RECORD val) throws ReadException {
		assert buffer != null;
		val.szName = buffer.readByteArray(20);
		val.unBoneIndex = buffer.readShort();
		val.cbColGroupIndex = buffer.readByte();
		val.unColGroupMask = buffer.readShort();
		val.cbShapeType = buffer.readByte();
		val.fWidth = buffer.readFloat();
		val.fHeight = buffer.readFloat();
		val.fDepth = buffer.readFloat();
		val.pos = buffer.readFloatArray(3);
		val.rotation = buffer.readFloatArray(3);
		val.fMass = buffer.readFloat();
		val.fLinearDamping = buffer.readFloat();
		val.fAngularDamping = buffer.readFloat();
		val.fRestitution = buffer.readFloat();
		val.fFriction = buffer.readFloat();
		val.cbRigidBodyType = buffer.readByte();
		return val;
	}
	
	public static PMD_IK_RECORD read(IReadBuffer buffer, PMD_IK_RECORD val) throws ReadException {
		assert buffer != null;
		
		// is.read((char*)&parent,sizeof(parent))
		val.parent = buffer.readShort();
		// is.read((char*)&to,sizeof(to))
		val.to = buffer.readShort();
		// is.read((char*)&num_link,sizeof(num_link))
		val.NumLink = buffer.readByte();
		// is.read((char*)&count,sizeof(count))
		val.count = buffer.readShort();
		// is.read((char*)&fact,sizeof(fact))
		val.fact = buffer.readFloat();
		// link.resize(num_link)
		// is.read((char*)&link[0],num_link*sizeof(WORD))
		val.link.clear();
		for (int i = 0; i < val.NumLink; i++) {
			val.link.add(buffer.readShort());
		}
		return val;
	}
	
	public static PMD_MORP_RECORD read(IReadBuffer buffer, PMD_MORP_RECORD val) throws ReadException {
		assert buffer != null;
		// is.read(name,sizeof(name))
		val.name = buffer.readByteArray(20);
		// is.read((char*)&vnum,sizeof(vnum))
		val.vnum = buffer.readInteger();
		// is.read((char*)&grp,sizeof(grp))
		val.grp = buffer.readByte();
		// mv.resize(vnum)
		// is.read((char*)&mv[0],vnum*sizeof(PMD_MORP_VERTEX_RECORD))
		val.mv.clear();
		for (int i = 0; i < val.vnum; i++) {
			val.mv.add((new PMD_MORP_VERTEX_RECORD()).read(buffer));
		}
		return val;
	}
	

	public static PMD_MATERIAL_RECORD read(IReadBuffer buffer, PMD_MATERIAL_RECORD val) throws ReadException {
		assert buffer != null;
		val.diffuse = (new MMD_COLOR4()).read(buffer);
		val.shininess = buffer.readFloat();
		val.specular = (new MMD_COLOR3()).read(buffer);
		val.ambient = (new MMD_COLOR3()).read(buffer);
		val.p12 = buffer.readShort();
		val.nEdges = buffer.readInteger();
		val.textureFileName = buffer.readByteArray(20);
		return val;
	}
	
	public static PMD_GRP_NAME_RECORD read(IReadBuffer buffer, PMD_GRP_NAME_RECORD val) throws ReadException {
		assert buffer != null;
		val.name = buffer.readByteArray(50);
		return val;
	}

	public static PMD_CONSTRAINT_RECORD read(IReadBuffer buffer, PMD_CONSTRAINT_RECORD val) throws ReadException {
		assert buffer != null;
		val.szName = buffer.readByteArray(20);
		val.ulRigidA = buffer.readLong();
		val.ulRigidB = buffer.readLong();
		val.pos = buffer.readFloatArray(3);
		val.rotation = buffer.readFloatArray(3);
		val.posLimitL = buffer.readFloatArray(3);
		val.posLimitU = buffer.readFloatArray(3);
		val.rotLimitL = buffer.readFloatArray(3);
		val.rotLimitU = buffer.readFloatArray(3);
		val.springPos = buffer.readFloatArray(3);
		val.springRot = buffer.readFloatArray(3);
		return val;
	}
	
	public static PMD_BONE_RECORD read(IReadBuffer buffer, PMD_BONE_RECORD val) throws ReadException {
		assert buffer != null;
		val.name = buffer.readByteArray(20);
		val.parent = buffer.readShort();
		val.to = buffer.readShort();
		val.kind = buffer.readByte();
		val.knum = buffer.readShort();
		val.pos = buffer.readFloatArray(3);
		return val;
	}
	
	////////////////////////////
	
	public static PMD_HEADER read_PMD_HEADER(IReadBuffer buffer) throws ReadException {
		return read(buffer, new PMD_HEADER());
	}
	
	public static PMD_INDEX_RECORD read_PMD_INDEX_RECORD(IReadBuffer buffer) throws ReadException {
		return read(buffer, new PMD_INDEX_RECORD());
	}
	
	public static PMD_VERTEX_RECORD read_PMD_VERTEX_RECORD(IReadBuffer buffer) throws ReadException {
		return read(buffer, new PMD_VERTEX_RECORD());
	}
	
	public static PMD_GRP_RECORD read_PMD_GRP_RECORD(IReadBuffer buffer) throws ReadException {
		return read(buffer, new PMD_GRP_RECORD());
	}
	
	public static PMD_MORP_VERTEX_RECORD read_PMD_MORP_VERTEX_RECORD(IReadBuffer buffer) throws ReadException {
		return read(buffer, new PMD_MORP_VERTEX_RECORD());
	}
	
	public static PMD_RIGID_BODY_RECORD read_PMD_RIGID_BODY_RECORD(IReadBuffer buffer) throws ReadException {
		return read(buffer, new PMD_RIGID_BODY_RECORD());
	}
	
	public static PMD_IK_RECORD read_PMD_IK_RECORD(IReadBuffer buffer) throws ReadException {
		return read(buffer, new PMD_IK_RECORD());
	}
	
	public static PMD_MORP_RECORD read_PMD_MORP_RECORD(IReadBuffer buffer) throws ReadException {
		return read(buffer, new PMD_MORP_RECORD());
	}
	
	public static PMD_MATERIAL_RECORD read_PMD_MATERIAL_RECORD(IReadBuffer buffer) throws ReadException {
		return read(buffer, new PMD_MATERIAL_RECORD());
	}
	
	public static PMD_GRP_NAME_RECORD read_PMD_GRP_NAME_RECORD(IReadBuffer buffer) throws ReadException {
		return read(buffer, new PMD_GRP_NAME_RECORD());
	}
	
	public static PMD_CONSTRAINT_RECORD read_PMD_CONSTRAINT_RECORD(IReadBuffer buffer) throws ReadException {
		return read(buffer, new PMD_CONSTRAINT_RECORD());
	}
	
	public static PMD_BONE_RECORD read_PMD_BONE_RECORD(IReadBuffer buffer) throws ReadException {
		return read(buffer, new PMD_BONE_RECORD());
	}
}
