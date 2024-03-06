package cn.paper_card.qq_group_command;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public interface GroupRootCommandApi {

    boolean addCommandForAdminMainGroup(@NotNull QqGroupCommand command);


    boolean addCommandForMemberMainGroup(@NotNull QqGroupCommand command);

    @Nullable String[] executeAdminMainGroupCommand(@NotNull String commandLine,
                                                    long memberId, @NotNull String memberName,
                                                    long groupId, @NotNull String groupName);

    @Nullable String[] executeMemberMainGroupCommand(@NotNull String commandLine,
                                                     long memberId, @NotNull String memberName,
                                                     long groupId, @NotNull String groupName);
}