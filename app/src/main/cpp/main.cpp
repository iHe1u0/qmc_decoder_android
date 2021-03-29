//
// Created by iMorning on 2021/3/29.
//
#include <jni.h>
#include <iostream>


int main() {
    std::cout << "Hello world,this is a test cpp file." << std::endl;
    return 0;
}

extern "C" JNIEXPORT jint JNICALL
Java_com_imorning_qmc_1decoder_MainActivity_main(JNIEnv *, jobject) {
    return main();
};