#include <jni.h>
#include <string>

#include "src/decoder.cpp"

extern "C" JNIEXPORT jstring JNICALL
Java_com_imorning_qmc_1decoder_MainActivity_mainProcess(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    std::vector<std::string> qmc = {};
    std::string decodePath = "";
    int result = main_qmc_process(qmc, decodePath);
    return env->NewStringUTF(hello.c_str());
}