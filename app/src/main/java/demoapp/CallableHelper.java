package demoapp;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Calling {@link Callable#call()} or Running {@link Runnable#run()} code
 * with a timeout based on {@link Future#get(long, TimeUnit))}
 * @author pascaldalfarra
 *
 */
public class CallableHelper
{

    private CallableHelper()
    {
    }

    public static final void run(final Runnable runnable, int timeoutInSeconds)
    {
        run(runnable, null, timeoutInSeconds);
    }

    public static final void run(final Runnable runnable, Runnable timeoutCallback, int timeoutInSeconds)
    {
        call(new Callable<Void>()
        {
            @Override
            public Void call() throws Exception
            {
                runnable.run();
                return null;
            }
        }, timeoutCallback, timeoutInSeconds);
    }

    public static final <T> T call(final Callable<T> callable, int timeoutInSeconds)
    {
        return call(callable, null, timeoutInSeconds);
    }

    public static final <T> T call(final Callable<T> callable, Runnable timeoutCallback, int timeoutInSeconds)
    {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        try
        {
            Future<T> future = executor.submit(callable);
            T result = future.get(timeoutInSeconds, TimeUnit.SECONDS);
            System.out.println("CallableHelper - Finished!");
            return result;
        }
        catch (TimeoutException e)
        {
            System.out.println("CallableHelper - TimeoutException!");
            if(timeoutCallback != null)
            {
                timeoutCallback.run();
            }
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        catch (ExecutionException e)
        {
            e.printStackTrace();
        }
        finally
        {
            executor.shutdownNow();
            executor = null;
        }

        return null;
    }

}