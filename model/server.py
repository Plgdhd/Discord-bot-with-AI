from flask import Flask, request, jsonify
from generate import generate_text  # из model/generate.py

app = Flask(__name__)

@app.route("/generate", methods=["POST"])
def generate():
    data = request.json
    prompt = data.get("prompt", "")
    words = 0
    words = data.get("words", "")
    if(words == 0): words = 3
    result = generate_text(prompt, words)
    return jsonify({"response": result})

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000)
