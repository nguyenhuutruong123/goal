package com.goal.service;


import java.io.IOException;

public interface ParentService {
    boolean saveGoal(Object object,String type) throws IOException;
}
