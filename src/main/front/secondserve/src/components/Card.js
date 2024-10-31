import React from 'react';
import { View, Text, StyleSheet } from 'react-native';

const Card = ({ text }) => {
  return (
    <View style={styles.card}>
      <Text>{text}</Text>
    </View>
  );
};

const styles = StyleSheet.create({
  card: {
    backgroundColor: '#fff',
    padding: 15,
    marginVertical: 10,
    borderRadius: 10,
    elevation: 2,
  },
});

export default Card;
