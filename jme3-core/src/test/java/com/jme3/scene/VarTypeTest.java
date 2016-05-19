package com.jme3.shader;

import com.jme3.texture.Image;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
import com.jme3.asset.TextureKey;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class VarTypeTest {
  @Test
  public void testGetValueAsStringFromTexture() {
    Texture mockTexture = Mockito.mock(Texture.class);
    TextureKey mockTextureKey = Mockito.mock(TextureKey.class);
    Image mockImage = Mockito.mock(Image.class);
    when(mockTexture.getKey()).thenReturn(mockTextureKey);
    when(mockTextureKey.isFlipY()).thenReturn(true);
    when(mockTexture.getWrap(Texture.WrapAxis.S)).thenReturn(Texture.WrapMode.Repeat);
    when(mockTexture.getWrap(Texture.WrapAxis.T)).thenReturn(Texture.WrapMode.Repeat);
    when(mockTexture.getWrap(Texture.WrapAxis.R)).thenReturn(Texture.WrapMode.Repeat);
    when(mockTexture.getImage()).thenReturn(mockImage);
    when(mockImage.hasMipmaps()).thenReturn(true);
    when(mockTexture.getMinFilter()).thenReturn(Texture.MinFilter.BilinearNoMipMaps);
    when(mockTexture.getMagFilter()).thenReturn(Texture.MagFilter.Bilinear);
    when(mockTextureKey.getName()).thenReturn("epicName");
    assert VarType.getValueAsStringFromTexture(mockTexture).equals("Flip WrapRepeat_S WrapRepeat_T WrapRepeat_R MinBilinearNoMipMaps \"epicName\"");
    // Testing when if are true
    when(mockTexture.getMinFilter()).thenReturn(Texture.MinFilter.NearestNoMipMaps);
    when(mockTexture.getMagFilter()).thenReturn(Texture.MagFilter.Nearest);
    assert VarType.getValueAsStringFromTexture(mockTexture).equals("Flip WrapRepeat_S WrapRepeat_T WrapRepeat_R MinNearestNoMipMaps MinNearest \"epicName\"");
  }
}