package com.jme3.input;

import java.util.List;

import com.jme3.input.Mapping;

public interface IReadBindings {

    public List<Mapping> getMappings(int hash);
    
    public boolean contains(int hash);
    
}