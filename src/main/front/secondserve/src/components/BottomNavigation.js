import React from 'react';
import { View, Text, StyleSheet } from 'react-native';

const BottomNavigation = () => {
  return (
    <View style={styles.bottomNavigation}>
      <Text>쇼핑</Text>
      <Text>홈</Text>
      <Text>콘텐츠</Text>
      <Text>클립</Text>
    </View>
  );
};

const styles = StyleSheet.create({
  bottomNavigation: {
    flexDirection: 'row',
    justifyContent: 'space-around',
    padding: 15,
    backgroundColor: '#fff',
    borderTopWidth: 1,
    borderTopColor: '#e0e0e0',
  },
});

export default BottomNavigation;
