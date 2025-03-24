import React, { useState, useEffect } from 'react';
import { View, Text, FlatList, TextInput, TouchableOpacity, StyleSheet } from 'react-native';
import axios from 'axios';
import AsyncStorage from '@react-native-async-storage/async-storage';

const ChatScreen = ({ route }: { route: any }) => {
  const [messages, setMessages] = useState<any[]>([]);
  const [messageInput, setMessageInput] = useState('');
  const [loading, setLoading] = useState(true);

  const { friendUsername } = route.params; // Arkadaşın kullanıcı adını al

  useEffect(() => {
    fetchMessages();
  }, [friendUsername]);

  const fetchMessages = async () => {
    try {
      const token = await AsyncStorage.getItem('jwtToken');
      if (!token) {
        alert('User not authenticated. Please log in.');
        return;
      }
  
      const response = await axios.get(
        'http://10.0.2.2:8080/messages/getmessages',
        {
          headers: {
            Authorization: `Bearer ${token}`,  // Token doğru şekilde header'a ekleniyor
          },
          params: {
            receiverName: friendUsername,  // Parametre doğru şekilde gönderiliyor
          },
        }
      );
      console.log("Messages: ", response.data);  // Mesajlar geldi mi, bunu kontrol et
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
  
      const response = await axios.post('http://10.0.2.2:8080/messages/send', {
        receiverUsername: friendUsername,
        content: messageInput,
        // Türkiye saatiyle timestamp alıyoruz
        timestamp: getTurkishDateTime(), // Türkiye saatiyle zaman bilgisini alıyoruz
      }, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
  
      // Yeni mesajı ekleyelim
      setMessages([
        ...messages,
        {
          senderUsername: 'You',
          receiverUsername: friendUsername,
          content: messageInput,
          timestamp: getTurkishDateTime(), // Türkiye saatiyle zaman bilgisi
        },
      ]);
      setMessageInput('');
    } catch (error) {
      console.error('Mesaj gönderilemedi:', error);
      alert('Mesaj gönderilemedi. Lütfen tekrar deneyin.');
    }
  };
  
  // Türkiye saatine göre tarih ve saat almayı sağlayan fonksiyon
  const getTurkishDateTime = () => {
    const date = new Date();
  
    // Türkiye saati için DateTimeFormat kullanıyoruz
    const turkishDateTime = new Intl.DateTimeFormat('tr-TR', {
      timeZone: 'Europe/Istanbul', // Türkiye'nin zaman dilimi
      hour12: false,               // 24 saat formatı
      weekday: 'long',             // Haftanın günü (örn. Pazartesi)
      year: 'numeric',             // Yıl
      month: 'long',               // Ay (örn. Ocak)
      day: 'numeric',              // Gün
      hour: '2-digit',             // Saat (2 basamaklı)
      minute: '2-digit',           // Dakika (2 basamaklı)
      second: '2-digit',           // Saniye (2 basamaklı)
    }).format(date);
  
    return turkishDateTime; // Türkiye saatiyle biçimlendirilmiş tarih ve saat
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
      <Text>Chat with {friendUsername}</Text>
      <FlatList
        data={messages}
        renderItem={renderMessage}
        keyExtractor={(item, index) => index.toString()}
      />
      <TextInput
        style={styles.input}
        value={messageInput}
        onChangeText={setMessageInput}
        placeholder="Message"
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

export default ChatScreen;
