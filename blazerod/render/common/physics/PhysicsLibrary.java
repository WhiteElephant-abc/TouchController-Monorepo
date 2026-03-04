package top.fifthlight.blazerod.physics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.fifthlight.blazerod.util.nativeloader.NativeLoader;

import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.DosFileAttributeView;
import java.nio.file.attribute.PosixFileAttributeView;
import java.nio.file.attribute.PosixFilePermission;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class PhysicsLibrary {
    private PhysicsLibrary() {
    }

    private static final Logger logger = LoggerFactory.getLogger(PhysicsLibrary.class);
    private static boolean isPhysicsAvailable = false;

    public native static long createPhysicsScene(ByteBuffer rigidBodies, ByteBuffer joints);

    public native static void destroyPhysicsScene(long physicsScene);

    public native static long createPhysicsWorld(long physicsScene, ByteBuffer initialTransform);

    public native static ByteBuffer getTransformBuffer(long physicsWorld);

    public native static void stepPhysicsWorld(long physicsWorld, float deltaTime, int maxSubSteps, float fixedTimeStep);

    public native static void resetRigidBody(long physicsWorld, int rigidBodyIndex,
                                             float px, float py, float pz,
                                             float qx, float qy, float qz, float qw);

    public native static void applyVelocityDamping(long physicsWorld, int rigidBodyIndex,
                                                   float linearAttenuation, float angularAttenuation);

    public native static void destroyPhysicsWorld(long physicsWorld);

    public static boolean isPhysicsAvailable() {
        return isPhysicsAvailable;
    }

    private static final List<Path> androidPaths = List.of(
            Path.of("/", "system", "build.prop"),
            Path.of("/", "system", "bin", "app_process"),
            Path.of("/", "system", "framework", "framework.jar")
    );

    public static boolean load() {
        if (isPhysicsAvailable) {
            return true;
        }

        logger.info("Loading bullet physics native library");

        try {
            NativeLoader.load(
                PhysicsLibrary.class.getClassLoader(), 
                "bullet", 
                "bullet"
            );
            isPhysicsAvailable = true;
            logger.info("Loaded bullet physics native library");
            return true;
        } catch (Exception | LinkageError ex) {
            logger.error("Failed to load bullet physics native library", ex);
            System.err.println("CRITICAL JNI LOAD ERROR:");
            ex.printStackTrace(System.err);
            return false;
        }
    }
}
