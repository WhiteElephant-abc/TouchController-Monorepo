package top.fifthlight.fastmerger.merger;

import org.jspecify.annotations.NonNull;
import top.fifthlight.bazel.worker.api.Worker;

import java.io.PrintWriter;
import java.nio.file.Path;

public class MergerWorker extends Worker {
    @Override
    protected int handleRequest(@NonNull PrintWriter out, @NonNull Path sandboxDir, String... args) throws Exception {
        return MergerCommand.invoke(out, args);
    }
}
