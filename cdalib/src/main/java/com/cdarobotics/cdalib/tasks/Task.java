package com.cdarobotics.cdalib.tasks;

import androidx.annotation.NonNull;

/**
 * Base type for all actions in the task framework. A task is initialized once via {@link #init()},
 * then {@link #run()} is polled every loop until it returns {@code true} to signal completion.
 *
 * <p>Tasks compose into larger behaviors through the fluent builders:
 * <ul>
 *     <li>{@link #andThen(Task...)} — run this task, then the given tasks in sequence</li>
 *     <li>{@link #alongWith(Task...)} — run this task in parallel with the given tasks; finishes when all finish</li>
 *     <li>{@link #raceWith(Task...)} — run this task in parallel with the given tasks; finishes when any one finishes</li>
 *     <li>{@link #withDeadline(Task)} — run this task until the given deadline task finishes</li>
 * </ul>
 */
public abstract class Task {

    /** Called once before the first {@link #run()}. Set up state and issue starting commands here. */
    abstract public void init();

    /**
     * Called every loop after {@link #init()}.
     *
     * @return {@code true} when the task is finished, {@code false} to keep running.
     */
    abstract public boolean run();

    @NonNull
    abstract public String toString();

    /** Runs this task, then the given tasks one after another. */
    public Task andThen(Task... next) {
        return new SequentialTask(prepend(this, next));
    }

    /** Runs this task in parallel with the given tasks, finishing once all of them finish. */
    public Task alongWith(Task... others) {
        return new ParallelTask(prepend(this, others));
    }

    /** Runs this task in parallel with the given tasks, finishing as soon as any one finishes. */
    public Task raceWith(Task... others) {
        return new RaceTask(prepend(this, others));
    }

    /** Runs this task until {@code deadline} finishes, then stops regardless of this task's state. */
    public Task withDeadline(Task deadline) {
        return new DeadlineTask(deadline, this);
    }

    private static Task[] prepend(Task first, Task[] rest) {
        Task[] all = new Task[rest.length + 1];
        all[0] = first;
        System.arraycopy(rest, 0, all, 1, rest.length);
        return all;
    }
}
