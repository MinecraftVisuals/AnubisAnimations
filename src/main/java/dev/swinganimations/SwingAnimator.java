package dev.swinganimations;

import net.minecraft.client.util.math.MatrixStack;
import org.joml.Quaternionf;

/** Swing math, ported 1:1 from the client's SwingAnimations module. */
public final class SwingAnimator {
    private static final float[] SIN = new float[65536];

    static {
        for (int i = 0; i < SIN.length; i++) {
            SIN[i] = (float) Math.sin(i * Math.PI * 2.0 / 65536.0);
        }
    }

    private SwingAnimator() {}

    private static float sin(double v) {
        return SIN[(int) (long) (v * 10430.378350470453) & 65535];
    }

    private static float sqrt(float v) {
        return (float) Math.sqrt(v);
    }

    private static void rotX(MatrixStack m, float deg) {
        m.multiply(new Quaternionf().rotationX(deg * 0.017453292f));
    }

    private static void rotY(MatrixStack m, float deg) {
        m.multiply(new Quaternionf().rotationY(deg * 0.017453292f));
    }

    private static void rotZ(MatrixStack m, float deg) {
        m.multiply(new Quaternionf().rotationZ(deg * 0.017453292f));
    }

    public static int duration(int base) {
        return Math.max(1, Math.round(base * 100.0f / SwingConfig.speed));
    }

    public static void offset(MatrixStack m) {
        m.translate(SwingConfig.offsetX * 0.01f, SwingConfig.offsetY * 0.01f, SwingConfig.offsetZ * 0.01f);
        float s = SwingConfig.scale / 100.0f;
        if (s != 1.0f) {
            m.scale(s, s, s);
        }
    }

    /** Returns true if the swing was replaced (vanilla swing should be cancelled). */
    public static boolean swing(MatrixStack m, float p, int side) {
        SwingStyle style = SwingConfig.style;
        if (style == SwingStyle.Vanilla) {
            return false;
        }
        float str = SwingConfig.strength / 100.0f;
        float s = side;
        float f6 = sin(sqrt(p) * 3.1415927f);
        float f7 = sin(p * 3.1415927f);
        float f8 = sin(p * p * 3.1415927f);

        switch (style) {
            case Smooth -> {
                m.translate(s * -0.25f * f6 * str,
                        0.12f * sin(sqrt(p) * 6.2831855f) * str,
                        -0.12f * f7 * str);
                rotY(m, s * (45.0f - 14.0f * f8 * str));
                rotZ(m, s * f6 * -14.0f * str);
                rotX(m, f6 * -58.0f * str);
                rotY(m, s * -45.0f);
            }
            case Slide -> {
                m.translate(s * 0.05f, 0.08f * str, -0.04f);
                rotY(m, s * 45.0f);
                rotZ(m, s * f6 * -10.0f * str);
                rotX(m, f6 * -35.0f * str);
                rotY(m, s * -45.0f);
                m.translate(0.0f, -0.25f * f6 * str, 0.0f);
            }
            case Stab -> {
                m.translate(s * -0.08f * f6 * str, 0.04f * f6 * str, -0.45f * f6 * str);
                rotX(m, -18.0f * f6 * str);
            }
            case Spin -> {
                m.translate(s * -0.10f * f6 * str, 0.05f * f6 * str, -0.10f * f7 * str);
                rotY(m, s * -360.0f * p);
            }
            case Swirl -> {
                m.translate(s * -0.15f * f6 * str, 0.08f * f6 * str, -0.10f * f7 * str);
                rotZ(m, s * -360.0f * p);
            }
            case Chop -> {
                m.translate(0.0f, 0.18f * f6 * str, -0.08f * f7 * str);
                rotX(m, -95.0f * f6 * str);
            }
            default -> {
                return false;
            }
        }
        return true;
    }
                            }
