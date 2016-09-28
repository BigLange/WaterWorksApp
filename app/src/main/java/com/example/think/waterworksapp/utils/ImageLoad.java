package com.example.think.waterworksapp.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.LruCache;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Semaphore;

/**
 * Created by Administrator on 2016/4/17.
 */
public class ImageLoad implements Runnable{
    // 本类就是图片加载类，用于图片选择器中的图片加载

    //图片缓存的核心对象
    private LruCache<String,Bitmap> mLruCache;

    //线程池
    private ExecutorService pool;
    //图片加载器使用单例模式
    private static ImageLoad imageLoad;
    //确定使用哪种加载策略(这个参数保存哪种枚举类型，就用哪种策略)，这里我们使用看到那一部分就先加载那一部分的策略
    private Type mType = Type.LIFO;

    //图片的加载策略,这里保存的是枚举类型
    public enum  Type{
        //两种类型都是自己定义的,第一种是，从头加载起
        //第二种策略是，用户看到那一部分就优先加载那一部分，比如用户一下子滑动到第1200张，那就先从1200那边开始加载。 前面的可以先不加载
        FIFO,LIFO
    }

    //这里创建一个集合，来保存任务线程，这个集合的作用和handler里面保存massage的massageQueue差不多
    private LinkedList<Runnable> mTaskQueue;

    //后台的一个轮询线程
//    private Thread mPoolThread;
    //定义的handler来给mTaskQueue发送消息
    private Handler mPoolThreadHandler;

    //用于设置页面图片的handler
    private Handler mUIHandler;

    //mPoolThreadHandler对象创建和调用都是在线程中的，是并行的，下面这个对象用于控制两个并行线程的先后顺序
    private Semaphore mSemaphorePoolThreadHandler = new Semaphore(0);

    private Semaphore mSemaphorePool;

    private ImageLoad(Type type){
        //让用户指定加载的类型
        into(type);
    }

    private void into(Type type) {
        //初始化操作启动线程的轮询,首先是获得线程池的操作
        pool = MyThreadPool.getMyThreadPool().getPool();
        //开始轮询操作
        pool.submit(this);

        //获取我们应用的最大可用内存
        int maxMemory = (int)Runtime.getRuntime().maxMemory();
        //设置我们图片缓存对象需要使用的内存
        int cacheMemory = maxMemory/8;

        //创建图片缓存对象
        mLruCache = new LruCache<String,Bitmap>(cacheMemory){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                //复写这个方法，这个方法的作用就是测量bitmap的大小
                //这个公式就是 每一行的字节数*高度
                return value.getRowBytes()*value.getHeight();
            }
        };

        //创建mTaskQueue
        mTaskQueue = new LinkedList<>();
        mType = type;


        //这里就是指定线程 设置为3 那么下面有三个线程运行的时候，调用了他的阻塞方法，第四个线程就会阻塞在哪里
        mSemaphorePool = new Semaphore(3);
    }


    public static ImageLoad getImageLoad(){
        if(imageLoad==null){
            synchronized (ImageLoad.class){
                if(imageLoad==null){
                    imageLoad = new ImageLoad(Type.LIFO);
                }
            }
        }
        return imageLoad;
    }


    public ArrayList<Bitmap> fromPathGetBitmap(ArrayList<String> paths){
        ArrayList<Bitmap> bitmaps = new ArrayList<>();
        for (String path:paths) {
            bitmaps.add(getBitmapFromLruCache(path));
        }
        return bitmaps;
    }

    //核心方法，根据给的path来给imageView设置图片
    public void loadImage(final String path, final ImageView imageView){
            imageView.setTag(path);

        if(mUIHandler==null){
            mUIHandler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                   //获取得到的图片,为imageView回调设置图片
                    ImageHodler mHodler = (ImageHodler)msg.obj;
                    Bitmap bitmap = mHodler.bitmap;
                    String paht = mHodler.path;
                    ImageView imageView = mHodler.imageView;

                    if(imageView.getTag().toString().equals(paht)){
                        //判断，如果开始的图片路径和后面传进来的图片的路径一致的话，我们才为他设置图片
                        imageView.setImageBitmap(bitmap);
                    }

                }
            };
        }
        //根据path在缓冲中获取bitmap
        Bitmap bm = getBitmapFromLruCache(path);
        if(bm!=null){
            refreash(path, imageView, bm);
        }else{
            addTasks(new Runnable(){
                @Override
                public void run() {
                    //这里就是用来加载图片的
                    //还需要涉及到图片的压缩等等
                    //1.获得图片需要显示的大小
                    Imagesize imagesize = getImageViewSize(imageView);
                    Bitmap bm = decodeSampledBitmapFromPath(imagesize.imageWidth,imagesize.imageHeight,path);
                    //吧图片加入到缓存
                    addBitmapToLruCache(path, bm);
                    refreash(path,imageView,bm);

                    //每次执行完以后，就将mSemaphorePool的资源释放出来
                    mSemaphorePool.release();
                }
            });
        }
    }

    //这个方法里面的代码多次得到试用，所以抽取出来
    private void refreash(String path,ImageView imageView,Bitmap bitmap){
        Message message = Message.obtain();
        ImageHodler mImgHodler = new ImageHodler();
        mImgHodler.bitmap = bitmap;
        mImgHodler.imageView = imageView;
        mImgHodler.path = path;
        message.obj = mImgHodler;
        mUIHandler.sendMessage(message);
    }

    //吧图片加入到缓存中去
    private void addBitmapToLruCache(String path, Bitmap bm) {
        //判断缓存中，如果没有这个bitmap对象的话就添加进去
        if(getBitmapFromLruCache(path)==null){
            //当然还要判断是否为空
            if(bm!=null){
                mLruCache.put(path,bm);
            }
        }

    }


    //根据图片需要显示的宽和高，对图片进行压缩
    private Bitmap decodeSampledBitmapFromPath(int imageWidth, int imageHeight, String path) {
        //获取图片的宽和高，并不把图片加载到内存中
        BitmapFactory.Options options = new BitmapFactory.Options();
        //inJustDecodeBounds这个参数设置为ture就是不把图片加载进内存，这个时候，只能获得一些图片的基本属性，以及能够通过options设置图片的一些属性
        options.inJustDecodeBounds = true;
        //这里就直接获得宽和高了
        BitmapFactory.decodeFile(path,options);

        options.inSampleSize = caculateInSize(options,imageWidth,imageHeight);

        //使用获取到的缩放比例，再次解析图片，这次就让图片加载进内存了
        options.inJustDecodeBounds = false;
        Bitmap bitmap =  BitmapFactory.decodeFile(path,options);

        return bitmap;
    }


    //根据图片实际的宽和高以及图片实际宽高来计算sampleSize
    private int caculateInSize(BitmapFactory.Options options, int imageWidth, int imageHeight) {
        int width = options.outWidth;
        int hright = options.outHeight;

        int inSampleSize = 1;

        if(width>imageWidth||hright>imageHeight){
            //这里是得到图片的一个缩放的比例
            int widthRadio = Math.round(width/imageWidth);
            int hrightRadio = Math.round(hright/imageHeight);
            //最后我们得到两个比例中间比较大的比例来返回，到主要方法中进行返回
            inSampleSize = Math.max(widthRadio,hrightRadio);
        }
        return inSampleSize;
    }


    //根据imageView获取适当的宽和高，来对图片进行压缩
    private Imagesize getImageViewSize(ImageView imageView) {
        Imagesize imagesize = new Imagesize();
        DisplayMetrics dm = imageView.getContext().getResources().getDisplayMetrics();
        //拿到ViewGroup的宽度的获取器
        ViewGroup.LayoutParams lp = imageView.getLayoutParams();
       int width = imageView.getWidth();
        if(width==0){
            //当imageView刚刚new出来的时候，我们可能会获取不到imageView的高度
            //所以我们的这里就需要做判断
            width = lp.width;//获取其在layout中声明的宽度
        }
        if(width<0){
            //但是如果imageView在其layout中声明的宽度是wrap_content 或者是match_parent的话，那么lp.width返回的值就会是-1和-2
            //所以我们还是需要 判断一下。给其赋值
            //拿到他的最大值
//            width = imageView.getMaxWidth();
            width = getImageViewFielValue(imageView,"mMaxWidth");
        }
        if(width <= 0){
            //上面进行判断以后，还有可能小于0 ,这个时候我们就干脆让他等于我们屏幕的宽度
            width = dm.widthPixels;
        }

        //这里就和上面一样的处理了,是复制上面的，注释没有改，凑合着看
        int height = imageView.getHeight();
        if(height==0){
            //当imageView刚刚new出来的时候，我们可能会获取不到imageView的高度
            //所以我们的这里就需要做判断
            height = lp.height;//获取其在layout中声明的宽度
        }
        if(height<0){
            //但是如果imageView在其layout中声明的宽度是wrap_content 或者是match_parent的话，那么lp.width返回的值就会是-1和-2
            //所以我们还是需要 判断一下。给其赋值
            //拿到他的最大值
//            height = imageView.getMaxHeight();
            height = getImageViewFielValue(imageView,"mMaxHeight");
        }
        if(height <= 0){
            //上面进行判断以后，还有可能小于0 ,这个时候我们就干脆让他等于我们屏幕的宽度
            height = dm.heightPixels;
        }
        imagesize.imageWidth = width;
        imagesize.imageHeight = height;
        return imagesize;
    }


    private static int getImageViewFielValue(Object object,String fieldName){
        int vlaue = 0;
        try {
        Field field = ImageView.class.getDeclaredField(fieldName);
        field.setAccessible(true);
            int fieldValue = field.getInt(object);
            if (fieldValue>0&&fieldValue<Integer.MAX_VALUE){
                vlaue = fieldValue;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return vlaue;
    }

    private  class Imagesize{
        int imageWidth;
        int imageHeight;
    }

    private synchronized void addTasks(Runnable runnable) {
        //创建一个task,并且放入taskqueue中间去
        mTaskQueue.add(runnable);
            try {
                if (mPoolThreadHandler==null)
                    //判断当mPoolThreadHandler等于空的时候，我们就调用acquire 只有等那边那个线程用mSemaphorePoolThreadHandler调用了某个方法后
                    //我们这边才会解除阻塞
                    mSemaphorePoolThreadHandler.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        mPoolThreadHandler.sendEmptyMessage(0x110);
    }

    private Bitmap getBitmapFromLruCache(String path) {
        //根据path在缓存中获取到bitmap
        return mLruCache.get(path);

    }


    @Override
    public void run() {
//        Looper的两个方法用于线程来轮询
        Looper.prepare();
        mPoolThreadHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                //线程池去取出一个任务进行执行
                pool.execute(getTask());

                try {
                    //这里阻塞住是要等那边的线程执行完毕了释放了mSemaphorePool才能够继续执行下去
                    mSemaphorePool.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        //代码执行到这里表示mPoolThreadHandler创建完毕了，通知一下，那边的阻塞就会恢复
        mSemaphorePoolThreadHandler.release();
        Looper.loop();
    }

    //从任务队列取出一个方法
    private Runnable getTask(){
        if(mType == Type.FIFO){
            //直接返回第一个(也就是从头加载起)
            return mTaskQueue.removeFirst();
        }else if(mType == Type.LIFO){
            //这里就直接返回最后 一个，这个最后一个就是我们用户当前正在看的位置一个获取图片的请求
            return mTaskQueue.removeLast();
        }
        return null;
    }

    //创建一个对象，保存bitmap信息
    private class ImageHodler{
        Bitmap bitmap;
        ImageView imageView;
        String path;
    }
}
