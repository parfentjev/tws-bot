package org.parfentjev.twsbot.core;

import org.telegram.abilitybots.api.sender.SilentSender;

public record Subscription(SilentSender sender, Long chatId) {
}
