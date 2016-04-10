package com.jme3;

import com.jme3.shader.Shader;
import com.jme3.shader.Uniform;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsSame.theInstance;

public class ShaderCharacterizationTest {
    private Shader shader;

    @Before
    public void init() {
        this.shader = new Shader();

        // When calling this.shader.getUniform() this has to be called first
        // we can therefore call it a precondition to getUniform
        this.shader.initialize();
    }

    @Test(expected = java.lang.AssertionError.class)
    public void getUniformFirstCharacterizationTest() {
        // Lets first see what happens when we call the function with an empty string
        // this produces an AssertionError, we cannot call this method with a random string
        this.shader.getUniform("blaat");
    }

    @Test
    public void getUniformSecondCharacterizationTest() {
        // Okay, from the sourcecode we have noted that if you give a string not starting with
        // m_ or g_, it will assert

        // we expect that if we give the function a uniform that does not exist, that it will create one,
        // therefore we will assert that the length of the uniform map will increase

        int numOfUniforms = this.shader.getUniformMap().size();
        this.shader.getUniform("m_thisOneIsObviouslyNew");

        assertThat(this.shader.getUniformMap().size(), is(numOfUniforms + 1));

        // We can therefore assume this to be true
    }

    @Test
    public void getUniformThirdCharacterizationTest() {
        // If we then call getUniform with a key we know that exist, we can assume to get the same item back we put into
        // that map
        Uniform toBeCompared = this.shader.getUniform("m_thisOneIsObviouslyNew");

        assertThat(this.shader.getUniform("m_thisOneIsObviouslyNew"), is(theInstance(toBeCompared)));
    }

}
