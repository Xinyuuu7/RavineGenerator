package Xinyuiii.RavineGenerator;

import com.seedfinding.mccore.rand.ChunkRand;
import com.seedfinding.mccore.util.pos.BPos;
import com.seedfinding.mccore.version.MCVersion;

import static java.lang.Math.*;

public class RavineGenerator {
    private final ChunkRand rand;
    private int startX, startY, startZ;
    private float yaw, pitch;
    private double verticalRadiusAtCenter;
    private int ravineLength;
    private int lowerYGuess, upperYGuess;
    private int xGuess, zGuess;
    private double x, y, z;
    // We usually need to ensure that lowerY is less than 8 and upperY is greater than 40
    private int lowerY, upperY;

    public RavineGenerator() {
        this.rand = new ChunkRand();
    }

    public boolean canSpawn(long worldSeed, int chunkX, int chunkZ) {
        this.rand.setCarverSeed(worldSeed, chunkX, chunkZ, MCVersion.v1_16_1);
        if (this.rand.nextFloat() > 0.02) {
            return false;
        }
        this.startX = chunkX * 16 + this.rand.nextInt(16);
        this.startY = this.rand.nextInt(this.rand.nextInt(40) + 8) + 20;
        this.startZ = chunkZ * 16 + this.rand.nextInt(16);
        this.yaw = this.rand.nextFloat() * (float) (PI * 2);
        this.pitch = (this.rand.nextFloat() - 0.5F) / 4.0F;
        this.verticalRadiusAtCenter = (1.5 + (double) ((this.rand.nextFloat() * 2.0F + this.rand.nextFloat()) * 2.0F)) * 3.0;
        this.ravineLength = 112 - this.rand.nextInt(28);
        // Approximations
        int middleMiddleY = (int) (this.y + (sin(this.pitch) * this.ravineLength / 2));
        this.lowerYGuess = (int) (middleMiddleY - this.verticalRadiusAtCenter);
        this.upperYGuess = (int) (middleMiddleY + this.verticalRadiusAtCenter + 1);
        float deltaHorizontal = (float) cos(this.pitch);
        this.xGuess = (int) (this.ravineLength / 2 * cos(this.yaw) * deltaHorizontal);
        this.zGuess = (int) (this.ravineLength / 2 * sin(this.yaw) * deltaHorizontal);
        return true;
    }

    public void generate() {
        long canyonSeed = this.rand.nextLong();
        this.rand.setSeed(canyonSeed);
        // It makes some sort of block mask or something
        for (int i = 0; i < 256; i++) {
            if (i == 0 || this.rand.nextInt(3) == 0) {
                this.rand.nextFloat();
                this.rand.nextFloat();
            }
        }
        float ravineShifterA = 0.0F;
        float ravineShifterB = 0.0F;
        for (int i = 0; i < (this.ravineLength / 2); i++) {
            float deltaHorizontal = (float) cos(this.pitch);
            float deltaY = (float) sin(this.pitch);
            this.rand.nextFloat();
            this.rand.nextFloat();
            this.x = startX + cos(this.yaw) * deltaHorizontal;
            this.y = startY + deltaY;
            this.z = startZ + sin(this.yaw) * deltaHorizontal;
            this.pitch *= 0.7F;
            this.pitch += ravineShifterB * 0.05F;
            this.yaw += ravineShifterA * 0.05F;
            ravineShifterB *= 0.8F;
            ravineShifterA *= 0.5F;
            ravineShifterB += (this.rand.nextFloat() - this.rand.nextFloat()) * this.rand.nextFloat() * 2.0F;
            ravineShifterA += (this.rand.nextFloat() - this.rand.nextFloat()) * this.rand.nextFloat() * 4.0F;
            this.rand.nextInt(4);
        }
        this.upperY = (int) (this.y + this.verticalRadiusAtCenter + 1);
        // MC code suggests additional -1, but it seems to usually not reach it
        this.lowerY = (int) (this.y - this.verticalRadiusAtCenter);
        if (this.lowerY < 1) {
            this.lowerY = 1;
        }
        if (this.upperY > 248) {
            this.upperY = 248;
        }
    }

    public BPos getStart() {
        return new BPos(this.startX, this.startY, this.startZ);
    }

    public BPos getGuess() {
        int yGuess = (this.lowerYGuess + this.upperYGuess) >> 1;
        return new BPos(this.xGuess, yGuess, this.zGuess);
    }

    public BPos getMiddle() {
        return new BPos((int) this.x, (int) this.y, (int) this.z);
    }

    public int getLowerY() {
        return this.lowerY;
    }

    public int getUpperY() {
        return this.upperY;
    }
}