package com.example.cameraorgallery

import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.RadioGroup
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.example.cameraorgallery.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    // Abrir a camera
    private val getCamera =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {

            val bitmap = it?.data?.extras?.get("data") as Bitmap
            binding.imageView.setImageBitmap(bitmap)
        }

    // Abrir a galeria
    private val getImage = registerForActivityResult(ActivityResultContracts.GetContent(),
        ActivityResultCallback {
            it?.let {
                binding.imageView.setImageURI(it)
            }
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        with(binding) {
            // Ação de clicar o botão e chamada do função do RadioGroup
            btEscolherImagem.setOnClickListener {
                ChooseImage()
            }
        }
    }
 // Função de escolhar a imagem no Radio Button
    private fun ChooseImage() {
        val dialogEscolha = Dialog(this)
        dialogEscolha.setCancelable(true)
        dialogEscolha.setContentView(R.layout.dialog_image)
        val radioGroupChoose: RadioGroup = dialogEscolha.findViewById(R.id.rgCameraOrGaleria)
        radioGroupChoose.setOnCheckedChangeListener { _, i ->
            when (i) {
                R.id.rbCamera -> { // se escolher a opção da camera
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    getCamera.launch(intent)
                    dialogEscolha.dismiss()
                }
                R.id.rbGaleria -> { // se escolher a opção da galeria
                    getImage.launch("image/")
                    dialogEscolha.dismiss()

                }
            }
        }
        dialogEscolha.show()
    }
}

