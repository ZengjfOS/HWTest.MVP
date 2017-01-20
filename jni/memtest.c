#include "memtest.h"

#define BUF_MAX 8092
typedef unsigned long volatile ulv ;
typedef unsigned long           ul ;

#define ALLFUNCONT   16 * 4
struct test tests = {
        .fp[0] = test_random_value ,
        .fp[1] = test_xor_comparison ,
        .fp[2] = test_sub_comparison ,
        .fp[3] = test_mul_comparison ,
        .fp[4] = test_div_comparison ,
        .fp[5] = test_or_comparison ,
        .fp[6] = test_and_comparison ,
        .fp[7] = test_seqinc_comparison ,
        .fp[7] = test_solidbits_comparison ,
        .fp[8] = test_blockseq_comparison ,
        .fp[9] = test_checkerboard_comparison ,
        .fp[10] = test_bitspread_comparison ,
        .fp[11] = test_bitflip_comparison ,
        .fp[12] = test_walkbits1_comparison ,
        .fp[13] = test_walkbits0_comparison ,
        .fp[14] = NULL ,
        .count = 0 ,
        .maxFunc   = ALLFUNCONT ,
        .stop      = 0 ,
};


int memtest(size_t wantsize) {
    tests.count = 0 ;
    tests.stop = 0 ;
    volatile void *buf = NULL;
    int retval = 0 , i;
    int val ;

    size_t wantbytes = (wantsize * 1024 * 1024);
    size_t halflen , count ;
    unsigned long volatile *bufa = NULL , *bufb = NULL ;

    /*
     * 对传进来的内存大小进行检查
     * 如果大于板子的最大空闲内存，将会出错
     */
    if ((wantsize << 10) > getval())
    {
        goto ERR1;
    }

    /*
     * 申请一块我们自己想要的大小的内存
     */
    buf = (void volatile *) malloc(wantbytes) ;
    if (buf == NULL)
    {
        goto ERR1;
    }

    bufa = (unsigned long volatile *)buf ;
    halflen = wantbytes / 2 ;
    bufb = (unsigned long volatile *) (buf + halflen) ;
    count = halflen / sizeof (unsigned long) ;

    int test_val = sysconf(_SC_PAGE_SIZE) ;

    val = test_stuck_address(bufa , wantbytes / sizeof(unsigned long)) ;
    if (val == 1)
    {
        retval |= 0x2 ;
    }
    if (val == 2)
    {
       goto ERR2 ;
    }

    /*
     * 如果给了一个stop 标志位 ， 说明要及时的停止检查退出
     */
    if (val == 2)
        goto ERR2 ;

    for (i = 0 ; ; i++)
    {
        if (tests.fp[i] == NULL)
            break ;

        val = tests.fp[i](bufa , bufb , count) ;
        if (val == 1)
            retval |= 0x4 ;
        if (val == 2)
            goto ERR2 ;
    }

    /*
     * 对申请的内存进行释放
     */
    free((void *)buf) ;


    return retval ;

    ERR2:
        free((void *)buf)  ;
        return -1 ;
    ERR1:
        return 1 ;
}

int getpercent()
{
    return (tests.count * 100 / tests.maxFunc) ;
}

void gotoout(int val)
{
    tests.stop = val ;
}

int getval(void)
{
    int fd , retval ;
    char buf[BUF_MAX] ;
    const char *str = "MemFree" ;
    char *restr = NULL , *tmpstr = NULL;

    fd = open("/proc/meminfo" , O_RDONLY) ;
    if (fd < 0)
    {
        perror("open meminfo fail  ") ;
        return fd ;
    }

    read(fd , buf , sizeof(buf)) ;

    restr = strstr(buf , str) ;
    while (restr++)
    {
        if (*restr >= '0' && *restr <= '9')
            break ;
    }

    for(tmpstr = restr ; ; tmpstr++)
    {
        if (*tmpstr == 'k')
        {
            *tmpstr = '\0';
            break ;
        }
    }

    retval = atoi(restr) ;

    close(fd) ;

    return retval ;
}


int test_stuck_address(unsigned long volatile *bufa , size_t count)
{
    ulv *p1 = bufa;
    unsigned int j = 0;
    size_t i , m;
    off_t physaddr;


    p1 = (ulv *) bufa;

    for (i = 0; i < count; i++)
    {
        if ((i % (count /4 )) == 0)
        {
            tests.count += 1;
            if (tests.stop == 1)
                return 2;
        }

        *p1 = ((j + i) % 2) == 0 ? (ul) p1 : ~((ul) p1);
        *p1++;
    }

    p1 = (ulv *) bufa;
    for (i = 0; i < count; i++, p1++)
    {
        if (*p1 != (((j + i) % 2) == 0 ? (ul) p1 : ~((ul) p1)))
        {
            return 1;
        }
    }

    return 0;

}

static int compare_regions(ulv *bufa, ulv *bufb, size_t count) {
    int r = 0;
    size_t i;
    ulv *p1 = bufa;
    ulv *p2 = bufb;

    for (i = 0; i < count; i++, p1++, p2++)
    {
        if (*p1 != *p2)
        {
            r = 1;
        }
    }

    return r;
}


int test_random_value(unsigned long volatile *bufa, unsigned long volatile *bufb, size_t count)
{
    unsigned int val ;
    ulv *p1 = bufa;
    ulv *p2 = bufb;
    ul j = 0;
    size_t i;

    val = time(NULL) ;

    for (i = 0; i < count; i++)
    {
        if ((i % (count / 4)) == 0)
        {
            tests.count++ ;
            if (tests.stop == 1)
                return 2 ;
        }

        *p1++ = *p2++ = val ;
    }

    return compare_regions(bufa, bufb, count);

}

int test_xor_comparison(unsigned long volatile *bufa, unsigned long volatile *bufb, size_t count)
{
    unsigned int val;
    ulv *p1 = bufa;
    ulv *p2 = bufb;
    size_t i;

    val = time(NULL) ;
    ul q = val ;

    for (i = 0; i < count; i++)
    {
        if ((i % (count /4)) == 0)
        {
            tests.count++ ;
            if (tests.stop == 1)
                return 2 ;
        }

        *p1++ ^= q;
        *p2++ ^= q;
    }

    return compare_regions(bufa, bufb, count);
}

int test_sub_comparison(unsigned long volatile *bufa, unsigned long volatile *bufb, size_t count)
{
    ulv *p1 = bufa;
    ulv *p2 = bufb;
    size_t i;
    ul q = time(NULL) ;

    for (i = 0; i < count; i++)
    {
        if ((i % (count /4)) == 0)
        {
            tests.count++ ;
            if (tests.stop == 1)
                return 2 ;
        }

        *p1++ -= q;
        *p2++ -= q;
    }

    return compare_regions(bufa, bufb, count);

}
int test_mul_comparison(unsigned long volatile *bufa, unsigned long volatile *bufb, size_t count)
{
    ulv *p1 = bufa;
    ulv *p2 = bufb;
    size_t i;
    ul q = time(NULL) ;

    for (i = 0; i < count; i++)
    {
        if ((i % (count /4)) == 0)
        {
            tests.count++ ;
            if (tests.stop == 1)
                return 2 ;
        }

        *p1++ *= q;
        *p2++ *= q;
    }
    return compare_regions(bufa, bufb, count);
}

int test_div_comparison(unsigned long volatile *bufa, unsigned long volatile *bufb, size_t count)
{
    ulv *p1 = bufa;
    ulv *p2 = bufb;
    size_t i;
    ul q = time(NULL) ;

    for (i = 0; i < count; i++)
    {
        if ((i % (count /4)) == 0)
        {
            tests.count++ ;
            if (tests.stop == 1)
                return 2 ;
        }

        if (!q)
        {
            q++;
        }
        *p1++ /= q;
        *p2++ /= q;
    }

    return compare_regions(bufa, bufb, count);

}

int test_or_comparison(unsigned long volatile *bufa, unsigned long volatile *bufb, size_t count)
{
    ulv *p1 = bufa;
    ulv *p2 = bufb;
    size_t i;
    ul q = time(NULL) ;

    for (i = 0; i < count; i++)
    {
        if ((i % (count /4)) == 0)
        {
            tests.count++ ;
            if (tests.stop == 1)
                return 2 ;
        }

        *p1++ |= q;
        *p2++ |= q;
    }

    return compare_regions(bufa, bufb, count);
}
int test_and_comparison(unsigned long volatile *bufa, unsigned long volatile *bufb, size_t count)
{
    ulv *p1 = bufa;
    ulv *p2 = bufb;
    size_t i;
    ul q = time(NULL) ;

    for (i = 0; i < count; i++)
    {
        if ((i % (count /4)) == 0)
        {
            tests.count++ ;
            if (tests.stop == 1)
                return 2 ;
        }

        *p1++ &= q;
        *p2++ &= q;
    }

    return compare_regions(bufa, bufb, count);

}
int test_seqinc_comparison(unsigned long volatile *bufa, unsigned long volatile *bufb, size_t count)
{
    ulv *p1 = bufa;
    ulv *p2 = bufb;
    size_t i;
    ul q = time(NULL) ;

    for (i = 0; i < count; i++)
    {
        if ((i % (count /4)) == 0)
        {
            tests.count++ ;
            if (tests.stop == 1)
                return 2 ;
        }
        *p1++ = *p2++ = (i + q);
    }

    return compare_regions(bufa, bufb, count);
}

int test_solidbits_comparison(unsigned long volatile *bufa, unsigned long volatile *bufb, size_t count)
{
    ulv *p1 = bufa;
    ulv *p2 = bufb;
    unsigned int j;
    ul q;
    size_t i;


    q = (j % 2) == 0 ? 0xffffffff : 0;
    p1 = (ulv *) bufa;
    p2 = (ulv *) bufb;

    for (i = 0; i < count; i++)
    {
        if ((i % (count /4)) == 0)
        {
            tests.count++ ;
            if (tests.stop == 1)
                return 2 ;
        }

        *p1++ = *p2++ = (i % 2) == 0 ? q : ~q;
    }

    if (compare_regions(bufa, bufb, count))
    {
        return 1;
    }

    return 0 ;
}
int test_checkerboard_comparison(unsigned long volatile *bufa, unsigned long volatile *bufb, size_t count)
{
    ulv *p1 = bufa;
    ulv *p2 = bufb;
    unsigned int j;
    ul q;
    size_t i;

    q = (j % 2) == 0 ? 0x55555555 : 0xaaaaaaaa;
    p1 = (ulv *) bufa;
    p2 = (ulv *) bufb;
    for (i = 0; i < count; i++)
    {
        if ((i % (count /4)) == 0)
        {
            tests.count++ ;
            if (tests.stop == 1)
            return 2 ;
        }

        *p1++ = *p2++ = (i % 2) == 0 ? q : ~q;
    }

    if (compare_regions(bufa, bufb, count))
    {
        return 1;
    }

    return 0;
}
int test_blockseq_comparison(unsigned long volatile *bufa, unsigned long volatile *bufb, size_t count)
{
    ulv *p1 = bufa;
    ulv *p2 = bufb;
    unsigned int j;
    size_t i;


    p1 = (ulv *) bufa;
    p2 = (ulv *) bufb;
    for (i = 0; i < count; i++)
    {
        if ((i % (count /4)) == 0)
        {
            tests.count++ ;
            if (tests.stop == 1)
            return 2 ;
        }

        *p1++ = *p2++ = ((j | j << 8 | j << 16 | j << 24)) ;
    }

    if (compare_regions(bufa, bufb, count))
    {
        return 1;
    }

    return 0 ;
}

int test_walkbits0_comparison(unsigned long volatile *bufa, unsigned long volatile *bufb, size_t count)
{
    ulv *p1 = bufa;
    ulv *p2 = bufb;
    unsigned int j;
    size_t i;


    j = 5 ;
    p1 = (ulv *) bufa;
    p2 = (ulv *) bufb;
    for (i = 0; i < count; i++)
    {
        if ((i % (count /4)) == 0)
        {
            tests.count++ ;
            if (tests.stop == 1)
            return 2 ;
        }

        if (j < 32)
        {
            *p1++ = *p2++ = 0x1 << j;
        }
        else
        {
            *p1++ = *p2++ = 0x1 << (32 * 2 - j - 1);
        }
    }

    if (compare_regions(bufa, bufb, count))
    {
        return 1;
    }

    return 0 ;
}
int test_walkbits1_comparison(unsigned long volatile *bufa, unsigned long volatile *bufb, size_t count)
{
    ulv *p1 = bufa;
    ulv *p2 = bufb;
    unsigned int j;
    size_t i;


    p1 = (ulv *) bufa;
    p2 = (ulv *) bufb;
    for (i = 0; i < count; i++)
    {
        if ((i % (count / 4)) == 0)
        {
            tests.count++ ;
            if (tests.stop == 1)
            return 2 ;
        }

        if (j < 32)
        {
            *p1++ = *p2++ = 0xffffffff ^ (0x1 << j);
        }
        else
        {
            *p1++ = *p2++ = 0xffffffff ^ (0x1 << (32 * 2 - j - 1));
        }
    }

    if (compare_regions(bufa, bufb, count))
    {
        return 1;
    }

    return 0 ;
}
int test_bitspread_comparison(unsigned long volatile *bufa, unsigned long volatile *bufb, size_t count)
{
    ulv *p1 = bufa;
    ulv *p2 = bufb;
    unsigned int j;
    size_t i;


    p1 = (ulv *) bufa;
    p2 = (ulv *) bufb;
    for (i = 0; i < count; i++)
    {
        if ((i % (count / 4)) == 0)
        {
            tests.count++ ;
            if (tests.stop == 1)
            return 2 ;
        }

        if (j < 32)
        {
            *p1++ = *p2++ = (i % 2 == 0)
                            ? (0x1 << j) | (0x1 << (j + 2))
                            : 0xffffffff ^ ((0x1 << j)
                                            | (0x1 << (j + 2)));
        }
        else
        {
            *p1++ = *p2++ = (i % 2 == 0)
                            ? (0x1 << (0x1 * 2 - 1 - j)) | (0x1 << (32 * 2 + 1 - j))
                                         : 0xffffffff ^ (0x1 << (32 * 2 - 1 - j)) ;
        }
    }

    if (compare_regions(bufa, bufb, count))
    {
        return 1;
    }

    return 0 ;
}

int test_bitflip_comparison(unsigned long volatile *bufa, unsigned long volatile *bufb, size_t count)
{
    ulv *p1 = bufa;
    ulv *p2 = bufb;
    unsigned int j, k;
    ul q;
    size_t i;


    for (j = 0; j < 8; j++)
    {
        if ((i % (count / 4)) == 0)
        {
            tests.count++ ;
            if (tests.stop == 1)
            return 2 ;
        }

        q = 0x1 << j;
        q = ~q;
        p1 = (ulv *) bufa;
        p2 = (ulv *) bufb;
        for (i = 0; i < count; i++)
        {
            *p1++ = *p2++ = (i % 2) == 0 ? q : ~q;
        }

        if (compare_regions(bufa, bufb, count))
        {
            return 1;
        }
    }

    return 0 ;
}


