//
//  ViewController.swift
//  SdkSample-UIKit
//
//  Created by Michael Charland on 2025-03-24.
//

import UIKit

class ViewController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
    }

    @IBAction func initFunctionCalled(_ sender: Any) {
        Task {
            do {
                try await theta.initialize()
            } catch {

            }
        }
    }
    
}

