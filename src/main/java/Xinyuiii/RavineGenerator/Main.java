package Xinyuiii.RavineGenerator;

import com.seedfinding.mcbiome.biome.Biome;
import com.seedfinding.mcbiome.biome.Biomes;
import com.seedfinding.mcbiome.source.OverworldBiomeSource;
import com.seedfinding.mccore.util.pos.BPos;
import com.seedfinding.mccore.version.MCVersion;

import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        long seed = 114514L;
        RavineGenerator generator = new RavineGenerator();
        OverworldBiomeSource obs = new OverworldBiomeSource(MCVersion.v1_16_1, seed);
        List<Biome> deepOcean = Arrays.asList(Biomes.DEEP_OCEAN, Biomes.DEEP_WARM_OCEAN, Biomes.DEEP_LUKEWARM_OCEAN, Biomes.DEEP_COLD_OCEAN, Biomes.DEEP_FROZEN_OCEAN);
        for (int chunkX = 0; chunkX <= 100; chunkX++) {
            for (int chunkZ = 0; chunkZ <= 100; chunkZ++) {
                if (!generator.canSpawn(seed, chunkX, chunkZ)) {
                    continue;
                }
                generator.generate();
                if (generator.getLowerY() > 7 || generator.getUpperY() < 41) {
                    continue;
                }
                BPos middle = generator.getMiddle();
                Biome biome = obs.getBiomeForNoiseGen(middle.getX(), 0, middle.getZ());
                if (!deepOcean.contains(biome)) {
                    continue;
                }
                System.out.printf("%d %d\n", chunkX << 4, chunkZ << 4);
            }
        }
    }
}