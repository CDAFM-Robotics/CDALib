package com.cdarobotics.cdalib.tasks;

/**
 * Drives a single active {@link Task} to completion, one loop at a time. Call {@link #addTask(Task)}
 * to queue work and {@link #update()} every loop; when idle it holds a {@link NullTask}.
 */
public class TaskMaster {
    private Task task = new NullTask();
    private boolean taskInitialized = false;

    /** Creates a task master that starts idle. */
    public TaskMaster() {
    }

    /** Creates a task master that begins by running the given task. */
    public TaskMaster(Task task) {
        this.task = task;
    }

    /** Queues a task: if idle it starts immediately, otherwise it runs after the current task finishes. */
    public void addTask(Task task) {
        if (this.task instanceof NullTask) {
            // Idle: the new task becomes the active one and must be initialized next update.
            this.task = task;
            taskInitialized = false;
        } else {
            // Busy: append after the running task. The already-initialized current task keeps
            // running (we don't reset taskInitialized), and SequentialTask initializes the
            // appended task once it advances to it.
            this.task = new SequentialTask(this.task, task);
        }
    }

    boolean status;

    /** Advances the active task by one step. Call once per loop. */
    public void update() {
        if (!taskInitialized) {
            task.init();
            taskInitialized = true;
        }

        status = task.run();
        if (status && !(task instanceof NullTask)) {
            task = new NullTask();
        }
    }

    /** @return the result of the most recent {@link #update()} — whether the active task finished. */
    public boolean getStatus() {
        return status;
    }

    /** @return the currently active task ({@link NullTask} when idle). */
    public Task getTask() {
        return task;
    }
}
