//
//  PhotoViewController.swift
//  SdkSample-UIKit
//
//  Created by Michael Charland on 2025-03-24.
//

import UIKit

class PhotoViewController: UIViewController {

    @IBOutlet weak var previewImageView: UIImageView!

    override func viewDidLoad() {
        super.viewDidLoad()

        Task {
            do {
                try await theta.livePreview(
                    frameHandler: { frame in
                        let preview = UIImage(data: frame)
                        DispatchQueue.main.async {
                            self.previewImageView.image = preview
                        }
                        return true
                    }
                )
            } catch {
            }
        }
    }

    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
        if isBeingDismissed {
            print("here")
        }
    }


    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */

}
