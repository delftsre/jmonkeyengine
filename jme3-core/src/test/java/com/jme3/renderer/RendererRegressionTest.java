package com.jme3.renderer;

import org.junit.Test;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class RendererRegressionTest {
    List<Box> blockedBoxes = Arrays.asList(new Box(0, 460, 200, 20));

    @Test
    public void testConeVSFustrum() throws IOException {
        assertTrue(isSameImage("imageData/TestConeVSFrustum-pre.png", "imageData/TestConeVSFrustum-post.png", blockedBoxes));
    }

    @Test
    public void testEnviromentMapping() throws IOException {
        assertTrue(isSameImage("imageData/TestEnviromentMapping-pre.png", "imageData/TestEnviromentMapping-post.png", blockedBoxes));
    }

    @Test
    public void testManyLights() throws IOException {
        assertTrue(isSameImage("imageData/TestManyLights-pre.png", "imageData/TestManyLights-post.png", blockedBoxes));
    }

    @Test
    public void testParallax() throws IOException {
        assertTrue(isSameImage("imageData/TestParallax-pre.png", "imageData/TestParallax-post.png", blockedBoxes));
    }

    @Test
    public void testPointLightShadows() throws IOException {
        assertTrue(isSameImage("imageData/TestPointLightShadows-pre.png", "imageData/TestPointLightShadows-post.png", blockedBoxes));
    }

    @Test
    public void testShaderNodes() throws IOException {
        assertTrue(isSameImage("imageData/TestShaderNodes-pre.png", "imageData/TestShaderNodes-post.png", blockedBoxes));
    }

    @Test
    public void testSpotlightTerrain() throws IOException {
        assertTrue(isSameImage("imageData/TestSpotlightTerrain-pre.png", "imageData/TestSpotlightTerrain-post.png", blockedBoxes));
    }

    @Test
    public void testTangentGen() throws IOException {
        assertTrue(isSameImage("imageData/TestTangentGen-pre.png", "imageData/TestTangentGen-post.png", blockedBoxes));
    }

    @Test
    public void testUnshadedModel() throws IOException {
        assertTrue(isSameImage("imageData/TestUnshadedModel-pre.png", "imageData/TestUnshadedModel-post.png", blockedBoxes));
    }

    @Test
    public void testManyLightsSingle() throws IOException {
        assertTrue(isSameImage("imageData/TestManyLightsSingle-pre.png", "imageData/TestManyLightsSingle-post.png", blockedBoxes));
    }

    private boolean isSameImage(String preImgName, String postImgName, List<Box> blockedRegions) throws IOException {
        BufferedImage preImg = ImageIO.read(getClass().getClassLoader().getResource(preImgName));
        BufferedImage postImg = ImageIO.read(getClass().getClassLoader().getResource(postImgName));

        for (Box b : blockedRegions) {
            blockRegion(preImg, b);
            blockRegion(postImg, b);
        }

        if (preImg.getWidth() != postImg.getWidth() || preImg.getHeight() != postImg.getHeight()) return false;

        for (int x = 0; x < preImg.getWidth(); x++) {
            for (int y = 0; y < preImg.getHeight(); y++) {
                if (preImg.getRGB(x, y) != postImg.getRGB(x, y)) {
                    return false;
                }
            }
        }

        return true;
    }

    private void blockRegion(BufferedImage img, Box blockedRegion) {
        for (int x = 0; x < blockedRegion.width; x++){
            for (int y = 0; y < blockedRegion.height; y++){
                // set this pixel to black
                img.setRGB(x + blockedRegion.x, y + blockedRegion.y, 0);
            }
        }
    }

    private class Box {
        int x;
        int y;

        int width;
        int height;

        public Box(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
    }


}
