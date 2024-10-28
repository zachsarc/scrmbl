# SCRMBL
## Welcome to SCRMBL, An Intelligent AI-powered Encryption/Decryption steganographic tool used to hide some top-secret data. Here is the sequence of SCRMBL using Code Name (no pun intended) "TRACR" as the Algorithm
# Image Encryption and Steganography Workflow

## 1. User Uploads Image
- The user uploads an image containing the information they want hidden or encrypted.
- The image is validated and sanitized to ensure it’s in a compatible format, such as `.jpg` or `.png`.

## 2. System Accesses Binary Data
- The image is opened and read as binary data.
- Libraries like Python’s **Pillow (PIL)** or **OpenCV** can be used to handle image file reading and manipulation.

## 3. Image Data Conversion to Binary
- The binary data of the image is extracted.
- This can be done by converting each byte to binary format, resulting in a long string or array of 0s and 1s.
- This binary data may represent pixel values, image metadata, etc.

## 4. Custom Algorithm Scrambles Binary Data
- This is where the custom algorithm is applied to scramble the binary data.
- Possible methods for scrambling:
  - **Rearranging bits or bytes**
  - **Applying mathematical transformations** (e.g., XOR operations)
  - **Encrypting the data** using a custom or standard cryptographic method (e.g., **AES**).
- The result is a unique array of numbers (or binary data) that represents the “scrambled” image.

## 5. Embedding Scrambled Data into a New Image
- To disguise the scrambled data in a way that still resembles a standard image, you could use:
  - **Steganography**: Hide the scrambled data within the pixels of an innocuous “cover image” by altering the least significant bits of each pixel.
  - **Noise Addition**: Alternatively, create a new image that appears as static or noise to the human eye but encodes your scrambled data.
- The final image looks normal or like stock imagery but actually contains the encrypted data.

## 6. Decryption Process
- When the user wants to retrieve their data, they upload the encrypted image back to the system.
- The system applies the decryption algorithm to extract and reconstruct the original data from the hidden binary.

## 7. User Interface
- A fully functional UI guides the user through each step, from uploading an image to decrypting and retrieving hidden data.
- This can be implemented as a web application using frontend frameworks like **React** or **Vue**, combined with a backend for processing the images and managing encryption (e.g., **Node.js**, **Flask**, **Django**).
