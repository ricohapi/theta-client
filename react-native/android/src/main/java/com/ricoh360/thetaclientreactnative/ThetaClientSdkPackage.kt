package com.ricoh360.thetaclientreactnative

import com.facebook.react.TurboReactPackage
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.module.model.ReactModuleInfo
import com.facebook.react.module.model.ReactModuleInfoProvider
import com.facebook.react.uimanager.ViewManager

class ThetaClientReactNativePackage : TurboReactPackage() {
  override fun getModule(name: String, reactContext: ReactApplicationContext): NativeModule? =
    ThetaClientReactNativeModule(reactContext).takeIf { name == ThetaClientReactNativeModule.NAME }

  override fun getReactModuleInfoProvider(): ReactModuleInfoProvider =
    ReactModuleInfoProvider {
      mutableMapOf(
        ThetaClientReactNativeModule.NAME to ReactModuleInfo(
          ThetaClientReactNativeModule.NAME,
          ThetaClientReactNativeModule.NAME,
          false,  // canOverrideExistingModule
          false,  // needsEagerInit
          true,   // hasConstants
          false,  // isCxxModule
          true    // isTurboModule - New Architecture only
        )
      )
    }

  override fun createViewManagers(reactContext: ReactApplicationContext): List<ViewManager<*, *>> =
    emptyList()
}
