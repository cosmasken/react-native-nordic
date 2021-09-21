//
//  RNNordicBleModule.swift
//  RNNordicBleModule
//
//  Copyright © 2021 Cosmas Ken. All rights reserved.
//

import Foundation

@objc(RNNordicBleModule)
class RNNordicBleModule: NSObject {
  @objc
  func constantsToExport() -> [AnyHashable : Any]! {
    return ["count": 1]
  }

  @objc
  static func requiresMainQueueSetup() -> Bool {
    return true
  }
}
