package com.knoldus.messagesModule.service;
import com.knoldus.messagesModule.model.MessageModel;
import com.knoldus.messagesModule.model.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {
    @Autowired
    private UserRepository userRepository;

    public  String sendData(MessageModel receiveMessageModel)
    {
        MessageModel messageModel = new MessageModel();
        messageModel.setSenderId(receiveMessageModel.getSenderId());
        messageModel.setReceiverId(receiveMessageModel.getReceiverId());
        messageModel.setMessageBody(receiveMessageModel.getMessageBody());
        messageModel.setDeleteReceiverMessage(false);
        messageModel.setDeleteSenderMessage(false);
        userRepository.save(messageModel);
        return "Saved SuccessFully";
    }

//    public List<MessageModel> getAllMessage()
//    {
//        return userRepository.findAll();
//    }

    public List<MessageModel> getMessageBySenderId(MessageModel messageModel)
    {
        return userRepository.findAllBySenderId(messageModel.getSenderId());
    }

    public List<MessageModel> getSpecificMessage(MessageModel messageModel)
    {
        System.out.println(messageModel.getSenderId());
        return userRepository.findAllBySenderIdAndReceiverId(messageModel.getSenderId(), messageModel.getReceiverId());
    }
    public String deleteMessageForMe(final MessageModel messageModel){
        if (userRepository.existsById(messageModel.getId())){
            MessageModel messageModelDeleteForMe = userRepository.getById(messageModel.getId());
            MessageService messageService = new MessageService();
            System.out.println(messageModel.getSenderId());
            //to check if user is sender
            if(userRepository.existsByIdAndSenderId(messageModel.getId(),
                    messageModel.getSenderId())) {
                // to check if receiver is deleteForMe is true
                System.out.println(messageModelDeleteForMe.isDeleteReceiverMessage());
                if (messageModelDeleteForMe.isDeleteReceiverMessage()) {
                    return messageService.deleteForEveryOne(messageModelDeleteForMe);
                } else {
                    messageModelDeleteForMe.setDeleteSenderMessage(true);
                    userRepository.save(messageModelDeleteForMe);
                }
            }

            //to check if user is receiver
            else if (userRepository.existsByIdAndReceiverId(messageModel.getId(),
                    messageModel.getSenderId())) {
                if (messageModelDeleteForMe.isDeleteReceiverMessage()) {
                    return messageService.deleteForEveryOne(messageModelDeleteForMe);
                } else {
                    messageModelDeleteForMe.setDeleteReceiverMessage(true);
                    userRepository.save(messageModelDeleteForMe);
                }
            }
            else {
                return "user is not present in this message";
            }
        }
        else {
            return "message does not exist";
        }
        return "message is deleted for me";
    }


    public String deleteForEveryOne(final MessageModel messageModel){
        System.out.println("751 data:"+messageModel.getId() +" \n"+ messageModel.getReceiverId()+" \n"+ messageModel.getSenderId()+" \n"+ messageModel.isDeleteReceiverMessage()+" \n"+ messageModel.isDeleteSenderMessage());

        userRepository.deleteById(messageModel.getId());
        return "message is deleted";
    }

    public List<MessageModel> getMessagesById(final MessageModel messageModel){
        System.out.println(messageModel.getSenderId());
        return userRepository.fetchMessageById(messageModel.getSenderId());
    }
}
