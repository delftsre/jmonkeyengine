package com.jme3.scene.plugins.fbx;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import com.jme3.asset.AssetInfo;
import com.jme3.asset.AssetKey;
import com.jme3.asset.AssetLoadException;
import com.jme3.asset.AssetManager;
import com.jme3.asset.ModelKey;
import com.jme3.material.MatParam;
import com.jme3.material.Material;
import com.jme3.material.MaterialDef;
import com.jme3.material.RenderState;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.plugins.fbx.file.FbxElement;
import com.jme3.scene.plugins.fbx.file.FbxFile;
import com.jme3.scene.plugins.fbx.file.FbxReader;
import com.jme3.scene.plugins.fbx.loaders.FbxMaterialLoader;
import com.jme3.scene.plugins.fbx.loaders.FbxTextureLoader;
import com.jme3.texture.Image;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Before;

@PrepareForTest({FbxReader.class, Node.class, SceneLoader.class, Material.class, FbxMaterialLoader.class, FbxTextureLoader.class, File.class})
@RunWith(PowerMockRunner.class)
public class SceneLoaderTest {
    @Mock
    public SceneKey sk;
    public ModelKey mk;
    public AssetInfo assetInfo;
    
    private SceneLoader sc;
    
    @Before
    public void setUp() {
        sc = new SceneLoader();
        sk = Mockito.mock(SceneKey.class);
        mk = Mockito.mock(ModelKey.class);
        assetInfo = Mockito.mock(AssetInfo.class);        
    }
    
    @Test(expected=NullPointerException.class)
    public void testSceneLoaderLoadWithNull() throws IOException {
        sc.load(null);
    }
    
    @Test(expected=AssetLoadException.class)
    public void testSceneLoaderWithAssetInfoMock() throws IOException {
        AssetManager assetMgrMock = Mockito.mock(AssetManager.class);
        Mockito.when(assetInfo.getManager()).thenReturn(assetMgrMock);       
        sc.load(assetInfo);
    }
    
    @Test
    public void testSceneLoaderMaterialGetsAttachedToMeshMock() throws Exception {
        AssetManager assetMgrMock = Mockito.mock(AssetManager.class);
        InputStream inputMock = Mockito.mock(InputStream.class);
        MaterialDef materialDefMock = Mockito.mock(MaterialDef.class);
        MatParam  matParamMock = Mockito.mock(MatParam.class);
        
        Node n = new Node();
        String childNodeName = "meshChildGetsMaterial";
        n.setName(childNodeName);
        Node meshNodeSpied = Mockito.spy(n);
        Node sceneNode = new Node();
        String sceneNodeName = "Fr-scene";
        sceneNode.setName(sceneNodeName);
        Node rootMeshNode = new Node();
        String rootMeshNodeName = "mesh";
        rootMeshNode.setName(rootMeshNodeName);
        Node rootMeshNodeSpied = Mockito.spy(rootMeshNode);
                
        FbxElement fbxMaterialMock = createElement(3, "Material", new Object[] {100L,  "abc\0def", ""});
        FbxElement fbxMeshMock = createElement(3, "Model", new Object[] {200L, "mesh\0ghi", "P"}); 
        FbxElement fbxMeshChildMock = createElement(3, "Model", new Object[] {300L, "meshChildGetsMaterial\0ghi", "P"}); 
        FbxElement fbxMaterialLinkMock = createElement(3, "C", new Object[] {"OO", 100L, 200L});
        FbxElement fbxRootNodeLinkMock = createElement(3, "C", new Object[] {"OO", 200L, 0L});
        FbxElement fbxMeshNodeLinkMock = createElement(3, "C", new Object[] {"OO", 300L, 200L});
        
        FbxElement fbxConnectionMock = new FbxElement(2);
        fbxConnectionMock.id = "Connections";
        fbxConnectionMock.children.add(fbxMaterialLinkMock);
        fbxConnectionMock.children.add(fbxRootNodeLinkMock);
        fbxConnectionMock.children.add(fbxMeshNodeLinkMock);
        
        FbxElement fbxElementMock = new FbxElement(2);
        fbxElementMock.id = "Objects";
        fbxElementMock.children.add(fbxMaterialMock);
        fbxElementMock.children.add(fbxMeshMock);
        fbxElementMock.children.add(fbxMeshChildMock);
        
        FbxFile fbxFileMock = new FbxFile();
        fbxFileMock.rootElements.add(fbxConnectionMock);
        fbxFileMock.rootElements.add(fbxElementMock);
        
        Mockito.when(assetInfo.getManager()).thenReturn(assetMgrMock);     
        Mockito.when(assetInfo.openStream()).thenReturn(inputMock);
        Mockito.when(assetInfo.getKey()).thenReturn(mk);
        
        Mockito.when(assetMgrMock.loadAsset(Mockito.any(AssetKey.class))).thenReturn(materialDefMock);
        
        Mockito.when(mk.getName()).thenReturn("Fromage");
        Mockito.when(mk.getExtension()).thenReturn("chee");
        
        Mockito.when(materialDefMock.getMaterialParam(Mockito.anyString())).thenReturn(matParamMock);   
        PowerMockito.mockStatic(FbxReader.class);       
        PowerMockito.when(FbxReader.readFBX(Mockito.any(InputStream.class))).thenReturn(fbxFileMock);
        
        //when meshChildGetsMaterial gets instantiated, put the spied object there instead:
        // same holds for rootMeshNode
        PowerMockito.whenNew(Node.class).withArguments(childNodeName).thenReturn(meshNodeSpied);
        PowerMockito.whenNew(Node.class).withArguments(rootMeshNodeName).thenReturn(rootMeshNodeSpied);
        PowerMockito.whenNew(Node.class).withArguments(sceneNodeName).thenReturn(sceneNode);
        
        Node scene = (Node) sc.load(assetInfo);
        Spatial mesh = scene.getChild("mesh");
        Spatial meshWithMaterial = ((Node) mesh).getChild("meshChildGetsMaterial");
        assertNotEquals(null, scene.getChild("mesh"));
        assertNotEquals(null, meshWithMaterial);
        
        Mockito.verify(meshNodeSpied, Mockito.times(1)).setMaterial(Mockito.any(Material.class));
        Mockito.verify(rootMeshNodeSpied, Mockito.never()).setMaterial(Mockito.any(Material.class));
    }
    
    @Test
    public void testSceneLoaderNoMaterialNoAttachmentToMesgMock() throws Exception {
        AssetManager assetMgrMock = Mockito.mock(AssetManager.class);
        InputStream inputMock = Mockito.mock(InputStream.class);
        
        Node n = new Node();
        String childNodeName = "meshChildNoMaterial";
        n.setName(childNodeName);
        Node meshNodeSpied = Mockito.spy(n);
        Node sceneNode = new Node();
        String sceneNodeName = "Fr-scene";
        sceneNode.setName(sceneNodeName);
        Node rootMeshNode = new Node();
        String rootMeshNodeName = "mesh";
        rootMeshNode.setName(rootMeshNodeName);
        Node rootMeshNodeSpied = Mockito.spy(rootMeshNode);
        
        FbxElement fbxMeshMock = createElement(3, "Model", new Object[] {200L, "mesh\0ghi", "P"}); 
        FbxElement fbxMeshChildMock = createElement(3, "Model", new Object[] {300L, "meshChildNoMaterial\0ghi", "P"}); 
        FbxElement fbxRootNodeLinkMock = createElement(3, "C", new Object[] {"OO", 200L, 0L});
        FbxElement fbxMeshNodeLinkMock = createElement(3, "C", new Object[] {"OO", 300L, 200L});
        
        FbxElement fbxConnectionMock = new FbxElement(2);
        fbxConnectionMock.id = "Connections";
        fbxConnectionMock.children.add(fbxRootNodeLinkMock);
        fbxConnectionMock.children.add(fbxMeshNodeLinkMock);
        
        FbxElement fbxElementMock = new FbxElement(2);
        fbxElementMock.id = "Objects";
        fbxElementMock.children.add(fbxMeshMock);
        fbxElementMock.children.add(fbxMeshChildMock);
        
        FbxFile fbxFileMock = new FbxFile();
        fbxFileMock.rootElements.add(fbxConnectionMock);
        fbxFileMock.rootElements.add(fbxElementMock);
        
        Mockito.when(assetInfo.getManager()).thenReturn(assetMgrMock);     
        Mockito.when(assetInfo.openStream()).thenReturn(inputMock);
        Mockito.when(assetInfo.getKey()).thenReturn(mk);       
        Mockito.when(mk.getName()).thenReturn("Fromage");
        Mockito.when(mk.getExtension()).thenReturn("chee"); 
        PowerMockito.mockStatic(FbxReader.class);       
        PowerMockito.when(FbxReader.readFBX(Mockito.any(InputStream.class))).thenReturn(fbxFileMock);
        //when meshChildGetsMaterial gets instantiated, put the spied object there instead:
        // same holds for rootMeshNode
        PowerMockito.whenNew(Node.class).withArguments(childNodeName).thenReturn(meshNodeSpied);
        PowerMockito.whenNew(Node.class).withArguments(rootMeshNodeName).thenReturn(rootMeshNodeSpied);
        PowerMockito.whenNew(Node.class).withArguments(sceneNodeName).thenReturn(sceneNode);
        
        Node scene = (Node) sc.load(assetInfo);
        Spatial mesh = scene.getChild(rootMeshNodeName);
        Spatial meshWithMaterial = ((Node) mesh).getChild(childNodeName);
        assertNotEquals(null, scene.getChild(rootMeshNodeName));
        assertNotEquals(null, meshWithMaterial);
        
        Mockito.verify(meshNodeSpied, Mockito.never()).setMaterial(Mockito.any(Material.class));
        Mockito.verify(rootMeshNodeSpied, Mockito.never()).setMaterial(Mockito.any(Material.class));
    }
    
    @Test
    public void testTextureGetsLinkedToMaterial() throws Exception {
        AssetManager assetMgrMock = Mockito.mock(AssetManager.class);
        InputStream inputMock = Mockito.mock(InputStream.class);
        Material matMock = Mockito.mock(Material.class);
        MaterialDef materialDefMock = Mockito.mock(MaterialDef.class);
        MatParam  matParamMock = Mockito.mock(MatParam.class);
        
        Node n = new Node();
        String childNodeName = "meshChildGetsMaterial";
        n.setName(childNodeName);
        Node meshNodeSpied = Mockito.spy(n);
        Node sceneNode = new Node();
        String sceneNodeName = "Fr-scene";
        sceneNode.setName(sceneNodeName);
        Node rootMeshNode = new Node();
        String rootMeshNodeName = "mesh";
        rootMeshNode.setName(rootMeshNodeName);
        Node rootMeshNodeSpied = Mockito.spy(rootMeshNode);
        
        String texModifier = "DiffuseColor";
        
        FbxElement fbxTextureMock = createElement(3, "Texture", new Object[] {500L, "mytex\0abc", ""});
        FbxElement fbxMaterialMock = createElement(3, "Material", new Object[] {100L,  "abc\0def", ""});
        FbxElement fbxMeshMock = createElement(3, "Model", new Object[] {200L, "mesh\0ghi", "P"}); 
        FbxElement fbxMeshChildMock = createElement(3, "Model", new Object[] {300L, "meshChildGetsMaterial\0ghi", "P"}); 
        FbxElement fbxTextureMaterialLinkMock = createElement(4, "C", new Object[] {"OP", 500L, 100L, texModifier });
        FbxElement fbxMaterialLinkMock = createElement(3, "C", new Object[] {"OO", 100L, 200L});
        FbxElement fbxRootNodeLinkMock = createElement(3, "C", new Object[] {"OO", 200L, 0L});
        FbxElement fbxMeshNodeLinkMock = createElement(3, "C", new Object[] {"OO", 300L, 200L});
        
        FbxElement fbxConnectionMock = new FbxElement(2);
        fbxConnectionMock.id = "Connections";
        fbxConnectionMock.children.add(fbxMaterialLinkMock);
        fbxConnectionMock.children.add(fbxRootNodeLinkMock);
        fbxConnectionMock.children.add(fbxMeshNodeLinkMock);
        fbxConnectionMock.children.add(fbxTextureMaterialLinkMock);
        
        FbxElement fbxElementMock = new FbxElement(2);
        fbxElementMock.id = "Objects";
        fbxElementMock.children.add(fbxTextureMock);
        fbxElementMock.children.add(fbxMaterialMock);
        fbxElementMock.children.add(fbxMeshMock);
        fbxElementMock.children.add(fbxMeshChildMock);
        
        FbxFile fbxFileMock = new FbxFile();
        fbxFileMock.rootElements.add(fbxConnectionMock);
        fbxFileMock.rootElements.add(fbxElementMock);
        
        Mockito.when(assetInfo.getManager()).thenReturn(assetMgrMock);     
        Mockito.when(assetInfo.openStream()).thenReturn(inputMock);
        Mockito.when(assetInfo.getKey()).thenReturn(mk);

        Mockito.when(mk.getName()).thenReturn("Fromage");
        Mockito.when(mk.getExtension()).thenReturn("chee");
        
        Mockito.when(assetMgrMock.loadAsset(Mockito.any(AssetKey.class))).thenReturn(materialDefMock);
        Mockito.when(materialDefMock.getMaterialParam(Mockito.anyString())).thenReturn(matParamMock);   
        Mockito.when(matMock.getAdditionalRenderState()).thenReturn(Mockito.mock(RenderState.class));
        
        PowerMockito.mockStatic(FbxReader.class);       
        PowerMockito.when(FbxReader.readFBX(Mockito.any(InputStream.class))).thenReturn(fbxFileMock);        
        //when meshChildGetsMaterial gets instantiated, put the spied object there instead:
        // same holds for rootMeshNode
        PowerMockito.whenNew(Node.class).withArguments(childNodeName).thenReturn(meshNodeSpied);
        PowerMockito.whenNew(Node.class).withArguments(rootMeshNodeName).thenReturn(rootMeshNodeSpied);
        PowerMockito.whenNew(Node.class).withArguments(sceneNodeName).thenReturn(sceneNode);
        PowerMockito.whenNew(Material.class).withAnyArguments().thenReturn(matMock);
        
        Node scene = (Node) sc.load(assetInfo);
        Spatial mesh = scene.getChild("mesh");
        Spatial meshWithMaterial = ((Node) mesh).getChild("meshChildGetsMaterial");
        assertNotEquals(null, scene.getChild("mesh"));
        assertNotEquals(null, meshWithMaterial);
        
        Mockito.verify(meshNodeSpied, Mockito.times(1)).setMaterial(Mockito.any(Material.class));
        Mockito.verify(rootMeshNodeSpied, Mockito.never()).setMaterial(Mockito.any(Material.class));
        Mockito.verify(matMock, Mockito.times(1)).setTexture(Mockito.anyString(), Mockito.any(Texture.class)); 
    }
    
    @Test
    public void testImagesAreLinkedToTextures() throws Exception {
        AssetManager assetMgrMock = Mockito.mock(AssetManager.class);
        InputStream inputMock = Mockito.mock(InputStream.class);
        Material matMock = Mockito.mock(Material.class);
        Texture getTexMock = Mockito.mock(Texture.class);
        Texture2D setTexMock = Mockito.mock(Texture2D.class);
        Image imageMock = Mockito.mock(Image.class);
        MaterialDef materialDefMock = Mockito.mock(MaterialDef.class);
        MatParam  matParamMock = Mockito.mock(MatParam.class);
        File fileMock = Mockito.mock(File.class);
        
        Node n = new Node();
        String childNodeName = "meshChildGetsMaterial";
        n.setName(childNodeName);
        Node meshNodeSpied = Mockito.spy(n);
        Node sceneNode = new Node();
        String sceneNodeName = "Fr-scene";
        sceneNode.setName(sceneNodeName);
        Node rootMeshNode = new Node();
        String rootMeshNodeName = "mesh";
        rootMeshNode.setName(rootMeshNodeName);
        Node rootMeshNodeSpied = Mockito.spy(rootMeshNode);
        
        String texModifier = "DiffuseColor";
        
        FbxElement fbxImageMock = createElement(3, "Video", new Object[] {700L, "myimg\0abc", "Clip"});
        fbxImageMock.children.add(createElement(2, "FileName", new Object[] { "kaas", "fromage" }));
        FbxElement fbxTextureMock = createElement(3, "Texture", new Object[] {500L, "mytex\0abc", ""});
        FbxElement fbxMaterialMock = createElement(3, "Material", new Object[] {100L,  "abc\0def", ""});
        FbxElement fbxMeshMock = createElement(3, "Model", new Object[] {200L, "mesh\0ghi", "P"}); 
        FbxElement fbxMeshChildMock = createElement(3, "Model", new Object[] {300L, "meshChildGetsMaterial\0ghi", "P"}); 
        FbxElement fbxImageTextureLinkMock = createElement(3, "C", new Object[] {"OO", 700L, 500L });
        FbxElement fbxTextureMaterialLinkMock = createElement(4, "C", new Object[] {"OP", 500L, 100L, texModifier });
        FbxElement fbxMaterialLinkMock = createElement(3, "C", new Object[] {"OO", 100L, 200L});
        FbxElement fbxRootNodeLinkMock = createElement(3, "C", new Object[] {"OO", 200L, 0L});
        FbxElement fbxMeshNodeLinkMock = createElement(3, "C", new Object[] {"OO", 300L, 200L});
        
        FbxElement fbxConnectionMock = new FbxElement(2);
        fbxConnectionMock.id = "Connections";
        fbxConnectionMock.children.add(fbxMaterialLinkMock);
        fbxConnectionMock.children.add(fbxRootNodeLinkMock);
        fbxConnectionMock.children.add(fbxMeshNodeLinkMock);
        fbxConnectionMock.children.add(fbxTextureMaterialLinkMock);
        fbxConnectionMock.children.add(fbxImageTextureLinkMock);
        
        FbxElement fbxElementMock = new FbxElement(2);
        fbxElementMock.id = "Objects";
        fbxElementMock.children.add(fbxTextureMock);
        fbxElementMock.children.add(fbxMaterialMock);
        fbxElementMock.children.add(fbxMeshMock);
        fbxElementMock.children.add(fbxMeshChildMock);
        fbxElementMock.children.add(fbxImageMock);
        
        FbxFile fbxFileMock = new FbxFile();
        fbxFileMock.rootElements.add(fbxConnectionMock);
        fbxFileMock.rootElements.add(fbxElementMock);
        
        Mockito.when(assetInfo.getManager()).thenReturn(assetMgrMock);     
        Mockito.when(assetInfo.openStream()).thenReturn(inputMock);
        Mockito.when(assetInfo.getKey()).thenReturn(mk);

        Mockito.when(mk.getName()).thenReturn("Fromage");
        Mockito.when(mk.getExtension()).thenReturn("chee");
        
        Mockito.when(fileMock.exists()).thenReturn(true);
        Mockito.when(fileMock.isFile()).thenReturn(true);
        Mockito.when(fileMock.getParent()).thenReturn("abc");
        
        Mockito.when(assetMgrMock.loadAsset(Mockito.any(AssetKey.class))).thenReturn(materialDefMock);
        Mockito.when(assetMgrMock.loadTexture(Mockito.anyString())).thenReturn(getTexMock);
        
        Mockito.when(getTexMock.getImage()).thenReturn(imageMock);
        Mockito.when(materialDefMock.getMaterialParam(Mockito.anyString())).thenReturn(matParamMock);   
        Mockito.when(matMock.getAdditionalRenderState()).thenReturn(Mockito.mock(RenderState.class));
        
        PowerMockito.mockStatic(FbxReader.class);       
        PowerMockito.when(FbxReader.readFBX(Mockito.any(InputStream.class))).thenReturn(fbxFileMock);        
        //when meshChildGetsMaterial gets instantiated, put the spied object there instead:
        // same holds for rootMeshNode
        PowerMockito.whenNew(Node.class).withArguments(childNodeName).thenReturn(meshNodeSpied);
        PowerMockito.whenNew(Node.class).withArguments(rootMeshNodeName).thenReturn(rootMeshNodeSpied);
        PowerMockito.whenNew(Node.class).withArguments(sceneNodeName).thenReturn(sceneNode);
        PowerMockito.whenNew(Material.class).withAnyArguments().thenReturn(matMock);
        PowerMockito.whenNew(File.class).withAnyArguments().thenReturn(fileMock);
        PowerMockito.whenNew(Texture.class).withAnyArguments().thenReturn(setTexMock);
        PowerMockito.whenNew(Texture2D.class).withAnyArguments().thenReturn(setTexMock);
        
        Node scene = (Node) sc.load(assetInfo);
        Spatial mesh = scene.getChild("mesh");
        Spatial meshWithMaterial = ((Node) mesh).getChild("meshChildGetsMaterial");
        assertNotEquals(null, scene.getChild("mesh"));
        assertNotEquals(null, meshWithMaterial);
        
        Mockito.verify(meshNodeSpied, Mockito.times(1)).setMaterial(Mockito.any(Material.class));
        Mockito.verify(rootMeshNodeSpied, Mockito.never()).setMaterial(Mockito.any(Material.class));
        Mockito.verify(matMock, Mockito.times(1)).setTexture(Mockito.anyString(), Mockito.any(Texture.class));
        Mockito.verify(getTexMock, Mockito.times(1)).getImage();
        Mockito.verify(setTexMock, Mockito.times(1)).setImage(Mockito.any(Image.class));
    }
    
    private FbxElement createElement(int length, String id, Object[] props) {
        FbxElement el = new FbxElement(length);
        el.id = id;
        for(int i = 0; i < length; i++)
            el.properties.add(props[i]);
        return el;
    }
}
