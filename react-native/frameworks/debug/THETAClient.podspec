Pod::Spec.new do |spec|
    spec.name                     = 'THETAClient'
    spec.version                  = '1.3.1'
    spec.homepage                 = 'https://github.com/ricohapi/theta-client'
    spec.source                   = { :http => 'https://github.com/ricohapi/theta-client/releases/download/1.3.1/THETAClient.xcframework.zip' }
    spec.authors                  = 'Ricoh Co, Ltd.'
    spec.license                  = 'MIT'
    spec.summary                  = 'THETA Client'
    spec.vendored_frameworks      = 'THETAClient.xcframework'
    spec.libraries                = 'c++'
    spec.ios.deployment_target = '14.0'
                
                
                
                
end