//
//  TakePhotoView.swift
//  SdkSample
//
import SwiftUI

class LiveCheck {

    private var timer: Timer?

    var lastUpdated: Date?
    var isNew = false

    @objc func fireTimer(timer: Timer) {
        if let lastUpdated {
            let last = lastUpdated.timeIntervalSince1970
            let now = Date().timeIntervalSince1970
            isNew = now - last < 5.0
        }
    }

    func preview(requestId: String) {
        let context = ["requestId": requestId]
        timer = Timer.scheduledTimer(timeInterval: 1.0,
                                     target: self,
                                     selector: #selector(fireTimer),
                                     userInfo: context,
                                     repeats: true)
    }
}

struct TakePhotoView: View {
    @Environment(\.presentationMode) var presentation
    @State var previewing = true
    @State var preview: UIImage?
    @State var isActive = false
    @State var item: FileItem?

    private let myTimer = LiveCheck()

    private let id: String

    init(id: String) {
        self.id = id
        myTimer.preview(requestId: id)
    }

    var body: some View {
        ZStack {
            previewView
                .frame(maxWidth: .infinity, maxHeight: .infinity)
                .ignoresSafeArea()
            VStack {
                Text("\(id) - \(myTimer.lastUpdated?.debugDescription ?? "Not Set")")
                   .background(myTimer.isNew ? .green : .red)
                Spacer()
                Button(
                    action: {
                        Task {
                            await takePhoto()
                        }
                    },
                    label: {
                        Image("shutter")
                    }
                )
                Spacer().frame(height: 8)
            }
            hiddenLink.frame(width: 0, height: 0)
        }
        .onAppear {
            self.previewing = true
            self.item = nil

            Task {
                do {
                    try await theta.livePreview(
                        frameHandler: { frame in

                            let preview = UIImage(data: frame)
                            myTimer.lastUpdated = Date()
                            self.preview = preview
                            return self.previewing
                        }
                    )
                } catch {
                    self.previewing = false
                }
            }
        }
        .onDisappear {
            self.previewing = false
        }
        .navigationBarBackButtonHidden(true)
        .navigationTitle("Take Photo")
        .navigationBarItems(
            leading:
            HStack {
                Button(action: {
                           previewing = false
                           DispatchQueue.main.asyncAfter(deadline: .now() + 0.1) {
                               self.presentation.wrappedValue.dismiss()
                           }
                       },
                       label: {
                           Image("chevron")
                               .resizable()
                               .frame(width: 24, height: 24)
                       })
            }
        )
    }

    var hiddenLink: some View {
        Group {
            if let fileItem = item {
                NavigationLink(
                    destination: PhotoSphereView(fileItem),
                    isActive: $isActive,
                    label: {}
                )
            } else {
                EmptyView()
            }
        }
    }

    var previewView: some View {
        Group {
            if let image = preview {
                Image(uiImage: image)
                    .resizable()
                    .scaledToFit()
            } else {
                ProgressView()
                    .scaleEffect(x: 2.0, y: 2.0)
                    .progressViewStyle(CircularProgressViewStyle(tint: Color(purple_700)))
            }
        }
    }

    func takePhoto() async {
        if !previewing {
            return
        }
        previewing = false
        DispatchQueue.main.asyncAfter(deadline: .now() + 0.1) {
            Task {
                do {
                    try await theta.takePicture { photoUrl in
                        // When nil, it is canceled.
                        if let photoUrl {
                            let parts = photoUrl.components(separatedBy: "/")
                            item = FileItem(
                                name: parts[parts.count - 1],
                                url: photoUrl
                            )
                        }
                        isActive = true
                    }
                } catch {
                    // TODO: handle error
                }
            }
        }
    }
}

struct TakePhotoView_Previews: PreviewProvider {
    static var previews: some View {
        TakePhotoView(id: "Preview")
    }
}

