package net.yzwlab.javammd.model;

import net.yzwlab.javammd.format.MMD_VERTEX_DESC;
import net.yzwlab.javammd.format.MMD_VERTEX_TEXUSE;

public class MMD_VERTEX_UNIT {
	public final MMD_VERTEX_DESC pOriginalVert;
	public final MMD_VERTEX_TEXUSE pCurrentVert;

	public MMD_VERTEX_UNIT(MMD_VERTEX_DESC pOriginalVert,
			MMD_VERTEX_TEXUSE pCurrentVert) {
		this.pOriginalVert = pOriginalVert;
		this.pCurrentVert = pCurrentVert;
	}
}
