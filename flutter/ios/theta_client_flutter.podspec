#
# To learn more about a Podspec see http://guides.cocoapods.org/syntax/podspec.html.
# Run `pod lib lint theta_client.podspec` to validate before publishing.
#
Pod::Spec.new do |s|
  s.name             = 'theta_client_flutter'
  s.version          = '1.12.0'
  s.summary          = 'theta-client plugin project.'
  s.description      = <<-DESC
  theta-client Flutter plugin project.
                       DESC
  s.homepage         = 'https://theta360.com/'
  s.license          = { :file => '../LICENSE' }
  s.author           = { 'Ricoh Co, Ltd.' => 'support@ricoh360.com' }
  s.source           = { :path => '.' }
  s.source_files = 'Classes/**/*'
  s.dependency 'Flutter'
  s.platform = :ios, '15.0'

  s.dependency 'THETAClient', '1.12.0'

  # Flutter.framework does not contain a i386 slice.
  s.pod_target_xcconfig = { 'DEFINES_MODULE' => 'YES', 'EXCLUDED_ARCHS[sdk=iphonesimulator*]' => 'i386' }
  s.swift_version = '5.0'
end
