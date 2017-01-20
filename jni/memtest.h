#ifndef __MEMTEST_CHEN_H_
#define __MEMTEST_CHEH_H__

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <fcntl.h>
#include <unistd.h>
#include <errno.h>
#include <sys/mman.h>
#include <time.h>
#include <jni.h>

#define FUNCMAX 16

//测试结构体
struct test {
    int (*fp[FUNCMAX])(unsigned long volatile *bufa , unsigned long volatile *bufb , size_t count);
    int count ;
    int maxFunc;
    int stop;
} ;

int getpercent(void) ;
void goout(int isstop) ;
int memtest(size_t want_m) ;
int getval(void);
int test_stuck_address(unsigned long volatile *bufa , size_t count);
int test_random_value(unsigned long volatile *bufa, unsigned long volatile *bufb, size_t count);
int test_xor_comparison(unsigned long volatile *bufa, unsigned long volatile *bufb, size_t count);
int test_sub_comparison(unsigned long volatile *bufa, unsigned long volatile *bufb, size_t count);
int test_mul_comparison(unsigned long volatile *bufa, unsigned long volatile *bufb, size_t count);
int test_div_comparison(unsigned long volatile *bufa, unsigned long volatile *bufb, size_t count);
int test_or_comparison(unsigned long volatile *bufa, unsigned long volatile *bufb, size_t count);
int test_and_comparison(unsigned long volatile *bufa, unsigned long volatile *bufb, size_t count);
int test_seqinc_comparison(unsigned long volatile *bufa, unsigned long volatile *bufb, size_t count);
int test_solidbits_comparison(unsigned long volatile *bufa, unsigned long volatile *bufb, size_t count);
int test_checkerboard_comparison(unsigned long volatile *bufa, unsigned long volatile *bufb, size_t count);
int test_blockseq_comparison(unsigned long volatile *bufa, unsigned long volatile *bufb, size_t count);
int test_walkbits0_comparison(unsigned long volatile *bufa, unsigned long volatile *bufb, size_t count);
int test_walkbits1_comparison(unsigned long volatile *bufa, unsigned long volatile *bufb, size_t count);
int test_bitspread_comparison(unsigned long volatile *bufa, unsigned long volatile *bufb, size_t count);
int test_bitflip_comparison(unsigned long volatile *bufa, unsigned long volatile *bufb, size_t count);



#endif /* __MEMTEST_CHEN_H__ */