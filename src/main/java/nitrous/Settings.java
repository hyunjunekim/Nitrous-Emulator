package nitrous;

import nitrous.lcd.Interpolator;

import java.awt.*;
import java.util.HashSet;
import java.util.prefs.Preferences;

public class Settings
{
    private static Preferences storage = Preferences.userNodeForPackage(Settings.class);

    private static <T extends Enum> T getEnum(String name, T def)
    {
        T[] values = (T[]) def.getDeclaringClass().getEnumConstants();
        int index = storage.getInt(name, def.ordinal());
        if (index < 0 || index >= values.length)
            index = def.ordinal();
        return values[index];
    }

    private static boolean channel1On;
    private static boolean channel2On;
    private static boolean channel3On;
    private static boolean channel4On;
    private static int magnification;

    public interface SpeedListener
    {
        void updateSpeed(EmulateSpeed speed);
    }

    private static EmulateSpeed speed;
    private static HashSet<SpeedListener> speedListeners = new HashSet<>();

    public interface InterpolatorListener
    {
        void updateInterpolator(Interpolator interpolator);
    }

    private static Interpolator interpolator;
    private static HashSet<InterpolatorListener> interpolatorListeners = new HashSet<>();

    static
    {
        channel1On = storage.getBoolean("channel1", true);
        channel2On = storage.getBoolean("channel2", true);
        channel3On = storage.getBoolean("channel3", true);
        channel4On = storage.getBoolean("channel4", true);

        speed = getEnum("speed", EmulateSpeed.SINGLE);
        interpolator = getEnum("interpolator", Interpolator.NEAREST);

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int maxMag = Math.min(screen.width / 160, screen.height / 144);
        magnification = Math.max(1, Math.min(maxMag, storage.getInt("magnification", 2)));
    }

    public static boolean isChannel1On()
    {
        return channel1On;
    }

    public static void setChannel1On(boolean channel1On)
    {
        Settings.channel1On = channel1On;
        storage.putBoolean("channel1", channel1On);
    }

    public static boolean isChannel2On()
    {
        return channel2On;
    }

    public static void setChannel2On(boolean channel2On)
    {
        Settings.channel2On = channel2On;
        storage.putBoolean("channel2", channel2On);
    }

    public static boolean isChannel3On()
    {
        return channel3On;
    }

    public static void setChannel3On(boolean channel3On)
    {
        Settings.channel3On = channel3On;
        storage.putBoolean("channel3", channel3On);
    }

    public static boolean isChannel4On()
    {
        return channel4On;
    }

    public static void setChannel4On(boolean channel4On)
    {
        Settings.channel4On = channel4On;
        storage.putBoolean("channel4", channel4On);
    }

    public static boolean isChannelOn(int channel)
    {
        switch (channel)
        {
            case 1:
                return channel1On;
            case 2:
                return channel2On;
            case 3:
                return channel3On;
            case 4:
                return channel4On;
            default:
                throw new IllegalArgumentException();
        }
    }

    public static void setChannelOn(int channel, boolean on)
    {
        switch (channel)
        {
            case 1:
                setChannel1On(on);
                break;
            case 2:
                setChannel2On(on);
                break;
            case 3:
                setChannel3On(on);
                break;
            case 4:
                setChannel4On(on);
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    public static void addSpeedListener(SpeedListener listener)
    {
        speedListeners.add(listener);
    }

    public static void removeSpeedListener(SpeedListener listener)
    {
        speedListeners.remove(listener);
    }

    public static EmulateSpeed getSpeed()
    {
        return speed;
    }

    public static void setSpeed(EmulateSpeed speed)
    {
        Settings.speed = speed;
        storage.putInt("speed", speed.ordinal());

        for (SpeedListener listener : speedListeners)
            listener.updateSpeed(speed);
    }

    public static void addInterpolatorListener(InterpolatorListener listener)
    {
        interpolatorListeners.add(listener);
    }

    public static void removeInterpolatorListener(InterpolatorListener listener)
    {
        interpolatorListeners.remove(listener);
    }

    public static Interpolator getInterpolator()
    {
        return interpolator;
    }

    public static void setInterpolator(Interpolator interpolator)
    {
        Settings.interpolator = interpolator;
    }

    public static int getMagnification()
    {
        return magnification;
    }

    public static void setMagnification(int magnification)
    {
        Settings.magnification = magnification;
    }
}