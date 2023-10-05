package cn.paper_card.qq_group_command;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("unused")
public abstract class QqGroupCommand {

    private final @NotNull String label;

    public QqGroupCommand(@NotNull String label) {
        this.label = label;
    }

    public @NotNull String getLabel() {
        return this.label;
    }

    public abstract @Nullable String[] execute(@NotNull String[] args,
                                               long sender, @NotNull String senderName,
                                               long groupId, @NotNull String groupName);

    public boolean canExecute(@NotNull String[] args, long sender, long groupId) {
        return true;
    }

    public static class HasSub extends QqGroupCommand {

        private final @NotNull ConcurrentHashMap<String, QqGroupCommand> commands;

        public HasSub(@NotNull String label) {
            super(label);
            this.commands = new ConcurrentHashMap<>();
        }

        public boolean addSubCommand(@NotNull QqGroupCommand command) {
            return this.commands.put(command.getLabel(), command) == null;
        }

        public @NotNull String[] onThisCommand(long sender, long group) {
            final StringBuilder builder = new StringBuilder();
            builder.append("可用的子命令：");
            for (final String label : this.commands.keySet()) {
                builder.append(" [%s]".formatted(label));
            }
            return new String[]{builder.toString()};
        }

        public @NotNull String[] onNotFoundCommand(@NotNull String label, long sender, @NotNull String senderName, long group, @NotNull String groupName) {
            final StringBuilder builder = new StringBuilder();
            builder.append("未知的子命令：%s，可用的子命令：".formatted(label));
            for (final String l : this.commands.keySet()) {
                builder.append(" [%s]".formatted(l));
            }
            return new String[]{builder.toString()};
        }

        @Override
        public @Nullable String[] execute(@NotNull String[] args, long sender, @NotNull String senderName, long groupId, @NotNull String groupName) {
            final String arg = args.length > 0 ? args[0] : null;

            if (arg == null) return this.onThisCommand(sender, groupId);

            final QqGroupCommand command = this.commands.get(arg);

            if (command == null) return this.onNotFoundCommand(arg, sender, senderName, groupId, groupName);

            final String[] newArgs = new String[args.length - 1];
            System.arraycopy(args, 1, newArgs, 0, newArgs.length);

            if (command.canExecute(newArgs, sender, groupId))
                return command.execute(newArgs, sender, senderName, groupId, groupName);

            return null;
        }
    }
}
