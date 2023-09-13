//
//  TakePhotoView.swift
//  SdkSample
//
import SwiftUI

struct TakePhotoView: View {
    @Environment(\.presentationMode) var presentation
    @State var previewing = true
    @State var preview: UIImage?
    @State var isActive = false
    @State var item: FileItem?

    var body: some View {
        ZStack {
            previewView
              .frame(maxWidth: .infinity, maxHeight: .infinity)
              .ignoresSafeArea()
            VStack {
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
        .onAppear() {
            self.previewing = true
            self.item = nil
            Task {
                do {
                    try await theta.livePreview(
                      frameHandler: {frame in
                          preview = UIImage(data: frame)
                          return self.previewing
                      }
                    )
                } catch {
                    self.previewing = false
                }
            }
        }
        .onDisappear() {
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
                    }
                    )
                }
        )
    }
    var hiddenLink: some View {
        Group {
            if let fileItem = item {
                NavigationLink(
                  destination: PhotoSphereView(fileItem),
                  isActive: $isActive,
                  label: {
                  })
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
        if (!previewing) {
            return
        }
        previewing = false
        DispatchQueue.main.asyncAfter(deadline: .now() + 0.1) {
            Task {
                do {
                    try await theta.takePicture {photoUrl in
                        let parts = photoUrl.components(separatedBy: "/")
                        item = FileItem(
                            name: parts[parts.count - 1],
                            url: photoUrl
                        )
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
        TakePhotoView()
    }
}
