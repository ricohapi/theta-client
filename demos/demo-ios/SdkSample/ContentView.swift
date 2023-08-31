//
//  ContentView.swift
//  SdkSample
//
import SwiftUI

let purple_700: UIColor = UIColor(hex: "6200EE")

struct ContentView: View {
    @State var getInfoError: Bool = false
    @Environment(\.scenePhase) var scenePhase

    init() {
        let appearance = UINavigationBarAppearance()
        appearance.configureWithOpaqueBackground()
        appearance.backgroundColor = purple_700
        appearance.titleTextAttributes = [.foregroundColor: UIColor.white]
        appearance.largeTitleTextAttributes = [.foregroundColor: UIColor.white]
        UINavigationBar.appearance().standardAppearance = appearance
        UINavigationBar.appearance().scrollEdgeAppearance = appearance
    }
    var body: some View {
        NavigationView {
            VStack(spacing: 16) {
                NavigationLink(destination: TakePhotoView()) {
                    Text("Take a photo")
                }
                  .padding(12)
                  .accentColor(Color.white)
                  .background(Color(purple_700))
                  .cornerRadius(10)

                NavigationLink(destination: ListPhotosView()) {
                    Text("List photos")
                }
                  .padding(12)
                  .accentColor(Color.white)
                  .background(Color(purple_700))
                  .cornerRadius(10)
            }
              .navigationTitle("Theta SDK sample app")
              .navigationBarTitleDisplayMode(.inline)
              .alert("warning", isPresented: $getInfoError) {
              } message: {
                  Text("Can not connect to Theta.")
              }
        }
        .onChange(of: scenePhase) { newPhase in
            if newPhase == .active {
                print("ContentView Active")
                theta.reset()
                Task {
                    do {
                        try await theta.info {info in
                            print(info)
                        }
                    } catch {
                        getInfoError = true
                    }
                }
            }
        }
        
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
