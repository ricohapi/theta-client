//
//  PhotoSphereView.swift
//  SdkSample
//
import SwiftUI

struct PhotoSphereView: View {
    @Environment(\.presentationMode) var presentation
    var item: FileItem
    init(_ item: FileItem = FileItem()) {
        self.item = item
    }
    var body: some View {
        VStack {
            SphereView(urlString: item.url)
        }
        .navigationBarBackButtonHidden(true)
        .navigationTitle(item.name)
        .navigationBarItems(
          leading:
            HStack {
                Button(action: {
                           self.presentation.wrappedValue.dismiss()
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
}

struct PhotoSphereView_Previews: PreviewProvider {
    static var previews: some View {
        PhotoSphereView()
    }
}
