import React, { useState, useEffect } from 'react';
import { View, Text, FlatList, TextInput, TouchableOpacity, StyleSheet } from 'react-native';
import axios from 'axios';
import AsyncStorage from '@react-native-async-storage/async-storage';

const GroupChatScreen = ({ route }: { route: any }) => {
  const [messages, setMessages] = useState<any[]>([]);
  const [messageInput, setMessageInput] = useState('');
  const [loading, setLoading] = useState(true);

  const { groupId } = route.params; // Grup ID'sini al

  useEffect(() => {
    fetchMessages();
  }, [groupId]);

  const fetchMessages = async () => {
    try {
      const token = await AsyncStorage.getItem('jwtToken');
      if (!token) {
        alert('User not authenticated. Please log in.');
        return;
      }
  
      const response = await axios.get(
        'http://10.0.2.2:8080/groups/messages', // API'nin URL'si
        {
          headers: {
            Authorization: `Bearer ${token}`,  
          },
          params: {
            groupId: groupId,  // groupId URL parametresi olarak gönderiliyor
          },
        }
      );
      console.log("Messages: ", response.data);  
      setMessages(response.data);
    } catch (error) {
      console.error('Mesajlar yüklenemedi:', error);
      alert('Unable to load messages. Please try again later.');
    } finally {
      setLoading(false);
    }
  };
  const sendMessage = async () => {
    if (!messageInput.trim()) {
      alert('Mesaj boş olamaz!');
      return;
    }
  
    try {
      const token = await AsyncStorage.getItem('jwtToken');
      if (!token) {
        alert('User not authenticated. Please log in.');
        return;
      }
  
      // URL'de groupId'yi path parametre olarak gönderiyoruz
      const response = await axios.post(
        `http://10.0.2.2:8080/groups/${groupId}/send`,  // groupId burada URL parametresi olarak gönderiliyor
        {
          content: messageInput,
            // timestamp de body'ye ekleniyor
        },
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );
  
      setMessages([
        ...messages,
        {
          senderUsername: 'You',
          content: messageInput,
          timestamp: getTurkishDateTime(),
        },
      ]);
      setMessageInput('');
    } catch (error) {
      console.error('Mesaj gönderilemedi:', error);
      alert('Mesaj gönderilemedi. Lütfen tekrar deneyin.');
    }
  };

  const getTurkishDateTime = () => {
    const date = new Date();

    const turkishDateTime = new Intl.DateTimeFormat('tr-TR', {
      timeZone: 'Europe/Istanbul',
      hour12: false,
      weekday: 'long',
      year: 'numeric',
      month: 'long',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
      second: '2-digit',
    }).format(date);

    return turkishDateTime;
  };

  const renderMessage = ({ item }: { item: any }) => (
    <View style={styles.messageContainer}>
      <Text>{item.senderUsername}: {item.content}</Text>
      <Text style={styles.timestamp}>{new Date(item.timestamp).toLocaleTimeString()}</Text>
    </View>
  );

  if (loading) {
    return <Text>Loading messages...</Text>;
  }

  return (
    <View style={styles.container}>
      <Text>Group Chat</Text>
      <FlatList
        data={messages}
        renderItem={renderMessage}
        keyExtractor={(item, index) => index.toString()}
      />
      <TextInput
        style={styles.input}
        value={messageInput}
        onChangeText={setMessageInput}
        placeholder="Type a message"
      />
      <TouchableOpacity onPress={sendMessage} style={styles.sendButton}>
        <Text style={styles.sendButtonText}>Send</Text>
      </TouchableOpacity>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 16,
  },
  messageContainer: {
    marginBottom: 10,
  },
  timestamp: {
    fontSize: 12,
    color: '#888',
  },
  input: {
    borderWidth: 1,
    padding: 10,
    marginVertical: 10,
    borderRadius: 5,
  },
  sendButton: {
    backgroundColor: '#007bff',
    padding: 10,
    borderRadius: 5,
  },
  sendButtonText: {
    color: 'white',
    textAlign: 'center',
  },
});

export default GroupChatScreen;
