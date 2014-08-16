package moe.shipduck.mmd;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.yzwlab.javammd.format.PMDFile;
import net.yzwlab.javammd.format.PMD_MATERIAL_RECORD;
import net.yzwlab.javammd.model.MMDMaterial;
import net.yzwlab.javammd.model.MMDVertexList;

public class MikuModel {
	PMDFile pmdFile;
	
	List<MikuMaterial> materialList;
	
	public Iterator<MikuMaterial> getMaterialIterator() {
		return materialList.iterator();
	}
	
	
	public MikuModel() {
	}
	
	public void setPMD(PMDFile pmdFile) {
		this.pmdFile = pmdFile;
		
		// init member variable
		materialList = new ArrayList<MikuMaterial>();
		
		MikuVertexList vertexList = new MikuVertexList(pmdFile.GetVertexChunk(), pmdFile.GetIndexChunk());
		
		List<PMD_MATERIAL_RECORD> rawMaterialList = pmdFile.GetMaterialChunk();
		for(PMD_MATERIAL_RECORD raw : rawMaterialList) {
			materialList.add(new MikuMaterial(raw));
		}
		
		int offset = 0;
		for (int i = 0; i < materialList.size(); i++) {
			offset = materialList.get(i).init(vertexList, offset);
		}
	}
}
