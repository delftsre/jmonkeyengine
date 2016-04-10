package com.jme3.renderer.opengl;

import com.jme3.texture.Image.Format;

public class StringBuilderForTextureUtil {
	
	static public String build(GLImageFormat[][] formats) {
		StringBuilder sb = new StringBuilder();
        sb.append("Supported texture formats: \n");
        for (int i = 0; i < Format.values().length; i++) {
            Format format = Format.values()[i];
            if (formats[0][i] != null) {
                boolean srgb = formats[1][i] != null;
                sb.append("\t").append(format.toString());
                sb.append(" (Linear");
                if (srgb) sb.append("/sRGB");
                sb.append(")\n");
            }
        }
		return sb.toString();
	}
}
