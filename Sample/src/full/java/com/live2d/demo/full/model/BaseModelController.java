package com.live2d.demo.full.model;

import com.live2d.demo.LAppDefine;
import com.live2d.demo.full.LAppPal;
import com.live2d.sdk.cubism.framework.CubismDefaultParameterId.ParameterId;
import com.live2d.sdk.cubism.framework.CubismFramework;
import com.live2d.sdk.cubism.framework.ICubismModelSetting;
import com.live2d.sdk.cubism.framework.id.CubismId;
import com.live2d.sdk.cubism.framework.id.CubismIdManager;
import com.live2d.sdk.cubism.framework.math.CubismMatrix44;
import com.live2d.sdk.cubism.framework.motion.ACubismMotion;
import com.live2d.sdk.cubism.framework.motion.CubismMotionManager;
import com.live2d.sdk.cubism.framework.motion.IBeganMotionCallback;
import com.live2d.sdk.cubism.framework.motion.IFinishedMotionCallback;

/**
 * Base class for all model-specific controllers
 * Provides common functionality for Live2D model management
 */
public abstract class BaseModelController {
    protected LAppModel model;
    protected String modelName;
    protected String modelPath;
    
    // Common parameters for all models
    protected final CubismId idParamAngleX;
    protected final CubismId idParamAngleY;
    protected final CubismId idParamAngleZ;
    protected final CubismId idParamBodyAngleX;
    protected final CubismId idParamEyeBallX;
    protected final CubismId idParamEyeBallY;
    
    // Motion callbacks
    protected static class BeganMotion implements IBeganMotionCallback {
        @Override
        public void execute(ACubismMotion motion) {
            if (LAppDefine.DEBUG_LOG_ENABLE) {
                LAppPal.printLog("Motion began");
            }
        }
    }
    
    protected static class FinishedMotion implements IFinishedMotionCallback {
        @Override
        public void execute(ACubismMotion motion) {
            if (LAppDefine.DEBUG_LOG_ENABLE) {
                LAppPal.printLog("Motion finished");
            }
        }
    }
    
    protected final BeganMotion beganMotion = new BeganMotion();
    protected final FinishedMotion finishedMotion = new FinishedMotion();
    
    public BaseModelController() {
        CubismIdManager idManager = CubismFramework.getIdManager();
        
        idParamAngleX = idManager.getId(ParameterId.ANGLE_X.getId());
        idParamAngleY = idManager.getId(ParameterId.ANGLE_Y.getId());
        idParamAngleZ = idManager.getId(ParameterId.ANGLE_Z.getId());
        idParamBodyAngleX = idManager.getId(ParameterId.BODY_ANGLE_X.getId());
        idParamEyeBallX = idManager.getId(ParameterId.EYE_BALL_X.getId());
        idParamEyeBallY = idManager.getId(ParameterId.EYE_BALL_Y.getId());
    }
    
    /**
     * Load and initialize the model
     * @param modelDir Directory containing the model files
     * @param modelJsonName Name of the model's JSON file
     */
    public void loadModel(String modelDir, String modelJsonName) {
        this.modelName = modelJsonName.replace(".model3.json", "");
        this.modelPath = modelDir;
        
        model = new LAppModel();
        model.loadAssets(modelDir, modelJsonName);
    }
    
    /**
     * Update the model's state
     */
    public void update() {
        if (model != null) {
            model.update();
        }
    }
    
    /**
     * Draw the model
     * @param projection Projection matrix
     */
    public void draw(CubismMatrix44 projection) {
        if (model != null) {
            model.draw(projection);
        }
    }
    
    /**
     * Handle touch events on the model
     * @param x X coordinate
     * @param y Y coordinate
     */
    public abstract void onTap(float x, float y);
    
    /**
     * Handle drag events
     * @param x X coordinate
     * @param y Y coordinate
     */
    public void onDrag(float x, float y) {
        if (model != null) {
            model.setDragging(x, y);
        }
    }
    
    /**
     * Release the model's resources
     */
    public void release() {
        if (model != null) {
            model.deleteModel();
            model = null;
        }
    }
    
    /**
     * Get the model instance
     * @return The LAppModel instance
     */
    public LAppModel getModel() {
        return model;
    }
    
    /**
     * Get the model's name
     * @return The model's name
     */
    public String getModelName() {
        return modelName;
    }
} 