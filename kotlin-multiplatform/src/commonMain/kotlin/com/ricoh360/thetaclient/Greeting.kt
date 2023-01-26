package com.ricoh360.thetaclient

class Greeting {
    fun greeting(): String {
        return "Hello, ${Platform().platform}!"
    }
}