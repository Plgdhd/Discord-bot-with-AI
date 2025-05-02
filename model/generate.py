import numpy as np
from tensorflow.keras.models import load_model
from tensorflow.keras.preprocessing.sequence import pad_sequences
import pickle

model = load_model("people_model.h5")
with open("tokenizer.pkl", "rb") as f:
    tokenizer = pickle.load(f)
max_seq_len = model.input_shape[1] + 1

def generate_text(seed_text, next_words=3):
    for _ in range(next_words):
        token_list = tokenizer.texts_to_sequences([seed_text])[0]
        token_list = pad_sequences([token_list], maxlen=max_seq_len - 1, padding='pre')
        predicted_probs = model.predict(token_list, verbose=0)
        predicted_index = np.argmax(predicted_probs)
        #пофиксить проблему с отправкой только одного слова боьавщ
        output_word = ""
        seed_text = ""
        for word, index in tokenizer.word_index.items():
            if index == predicted_index:
                output_word = word
                break
        seed_text +=  output_word
    return seed_text
