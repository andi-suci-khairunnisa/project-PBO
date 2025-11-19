package wahdini.getajobcopy.service;

import wahdini.getajobcopy.model.Message;
import wahdini.getajobcopy.repository.MessageRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    public Message sendMessage(Long conversationId, Long senderId, String text) {
        Message msg = new Message(conversationId, senderId, text);
        return messageRepository.save(msg);
    }

    public List<Message> getMessages(Long conversationId) {
        return messageRepository.findByConversationIdOrderBySentAtAsc(conversationId);
    }
}
