package com.skyeshade.mildlyusefuladditions.util;



import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.concurrent.ConcurrentLinkedQueue;

public class TickScheduler {
    private static final ConcurrentLinkedQueue<ScheduledTask> tasks = new ConcurrentLinkedQueue<>();

    public static void schedule(int delay, Runnable task) {
        tasks.add(new ScheduledTask(delay, task));
    }

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
            tasks.removeIf(task -> {
                task.delay--;
                if (task.delay <= 0) {
                    task.task.run();
                    return true;
                }
                return false;
            });
    }

    private static class ScheduledTask {
        int delay;
        Runnable task;

        ScheduledTask(int delay, Runnable task) {
            this.delay = delay;
            this.task = task;
        }
    }

    public static void register() {
        NeoForge.EVENT_BUS.register(TickScheduler.class);
    }
}
