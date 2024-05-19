package br.com.noobs.agronoobs.ui.operation.create

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import br.com.noobs.agronoobs.R
import br.com.noobs.agronoobs.dto.CreateOperationDto
import br.com.noobs.agronoobs.model.Operations
import br.com.noobs.agronoobs.provider.OperationProvider
import br.com.noobs.agronoobs.ui.components.LoadingDialogBar
import br.com.noobs.agronoobs.ui.login.Login
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import java.io.ByteArrayOutputStream

class CreateOperation : Fragment() {

    private val viewModel: CreateOperationViewModel by viewModels()

    private val user = FirebaseAuth.getInstance().currentUser
    private val provider = OperationProvider()
    private lateinit var loadingDialogBar: LoadingDialogBar
    private lateinit var spinnerLocations: Spinner
    private lateinit var spinnerOperations: Spinner
    private lateinit var defineLocationView: View
    private lateinit var defineStart: View
    private lateinit var defineFinish: View
    private lateinit var defineAcquiredQuantity: View
    private lateinit var nextButton: Button
    private lateinit var backButton: Button
    private lateinit var sendImageStart: Button
    private lateinit var positionStart: TextInputEditText
    private lateinit var imageStart: ImageView
    private lateinit var sendImageFinish: Button
    private lateinit var positionFinish: TextInputEditText
    private lateinit var imageFinish: ImageView
    private lateinit var acquiredQuantity: TextInputEditText

    private var position = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val inflate = inflater.inflate(R.layout.fragment_create_operation, container, false)

        user ?: startActivity(Intent(context, Login::class.java))

        val dtoCreation = CreateOperationDto(
            user!!.uid
        )

        loadingDialogBar = LoadingDialogBar(requireContext())

        defineLocationView = inflate.findViewById(R.id.define_location_ll)
        defineStart = inflate.findViewById(R.id.define_start_ll)
        defineFinish = inflate.findViewById(R.id.define_finish_ll)
        defineAcquiredQuantity = inflate.findViewById(R.id.define_acquired_ll)
        nextButton = inflate.findViewById(R.id.next_button)
        backButton = inflate.findViewById(R.id.back_button)


        nextButton.setOnClickListener {
            when (position) {
                0 -> {

                    if (dtoCreation.isDefineLocationValid()) {
                        backButton.text = getText(R.string.back)
                        defineLocationView.visibility = View.GONE
                        defineStart.visibility = View.VISIBLE
                        position++
                    } else {
                        Toast.makeText(context, "Definas nas opções", Toast.LENGTH_SHORT).show()
                    }

                }

                1 -> {
                    if (dtoCreation.selectedOperation != Operations.HARVEST) {
                        nextButton.text = getString(R.string.finish)
                    } else {
                        nextButton.text = getString(R.string.next)
                    }

                    if (dtoCreation.isDefineStartValid()) {
                        defineStart.visibility = View.GONE
                        defineFinish.visibility = View.VISIBLE
                        position++
                    } else {
                        Toast.makeText(
                            context,
                            "Defina posição inicial e imagem de prova",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                2 -> {
                    if (dtoCreation.isDefineFinishValid()) {
                        if (dtoCreation.selectedOperation!! == Operations.HARVEST) {
                            defineFinish.visibility = View.GONE
                            defineAcquiredQuantity.visibility = View.VISIBLE
                            nextButton.text = getString(R.string.finish)
                            position++
                        } else {
                            createOperation(dtoCreation)
                        }

                    } else {
                        Toast.makeText(
                            context,
                            "Defina posição final e imagem de prova",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                3 -> {
                    if (dtoCreation.isAcquiredQuantityValid()) {
                        createOperation(dtoCreation)
                    } else {
                        Toast.makeText(
                            context,
                            getString(R.string.define_collected_value),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            }
        }

        backButton.setOnClickListener {
            when (position) {
                0 -> {
                    activity?.onBackPressedDispatcher?.onBackPressed()
                }

                1 -> {
                    backButton.text = getText(R.string.cancel)
                    defineLocationView.visibility = View.VISIBLE
                    defineStart.visibility = View.GONE
                    position--
                }

                2 -> {
                    nextButton.text = getString(R.string.next)
                    defineStart.visibility = View.VISIBLE
                    defineFinish.visibility = View.GONE
                    position--
                }

                3 -> {
                    nextButton.text = getString(R.string.next)
                    defineFinish.visibility = View.VISIBLE
                    defineAcquiredQuantity.visibility = View.GONE
                    position--

                }
            }
        }



        handleDefineLocationView(inflate, inflater, dtoCreation)

        handleImageStart(inflate, dtoCreation)

        imageFinishHandle(inflate, dtoCreation)

        handleAcquiredQuantity(inflate, dtoCreation)











        return inflate
    }

    private fun handleAcquiredQuantity(inflate: View, dtoCreation: CreateOperationDto) {
        acquiredQuantity = inflate.findViewById(R.id.define_acquired_quantity)
        acquiredQuantity.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                dtoCreation.acquiredQuantity = s.toString().toFloatOrNull()
            }

        })
    }

    private fun createOperation(
        dtoCreation: CreateOperationDto,
    ) {

        loadingDialogBar.showDialog("Criando operação")

        provider.createOperation(dtoCreation, {
            loadingDialogBar.dismissDialog()
            Toast.makeText(context, "Operação criada com sucesso", Toast.LENGTH_SHORT).show().also {
                findNavController().navigate(R.id.action_navAddOperation_to_nav_home)
            }

        }, {
            loadingDialogBar.dismissDialog()
            Toast.makeText(context, "Erro ao criar operação", Toast.LENGTH_SHORT).show()
        })
    }

    private fun imageFinishHandle(
        inflate: View,
        dtoCreation: CreateOperationDto
    ) {
        imageFinish = inflate.findViewById(R.id.image_view_finish)
        sendImageFinish = inflate.findViewById(R.id.send_image_finish_button)
        positionFinish = inflate.findViewById(R.id.define_start_position)


        positionFinish.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                dtoCreation.finishPosition = s.toString().toFloatOrNull()
            }
        })


        val loadImage = registerForActivityResult(ActivityResultContracts.GetContent()) {
            imageFinish.setImageURI(it)
            dtoCreation.imageFinish = imageFinish.toByteArray()
        }

        sendImageFinish.setOnClickListener {
            loadImage.launch("image/*")
        }
    }

    private fun ImageView.toByteArray(): ByteArray {
        val bitmapDrawable = this.drawable as BitmapDrawable
        val baos = ByteArrayOutputStream()
        bitmapDrawable.bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        return baos.toByteArray()
    }

    private fun handleImageStart(
        inflate: View,
        dtoCreation: CreateOperationDto
    ) {
        imageStart = inflate.findViewById(R.id.image_view_start)
        sendImageStart = inflate.findViewById(R.id.send_image_start_button)
        positionStart = inflate.findViewById(R.id.define_start_position)


        positionStart.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                dtoCreation.startPosition = s.toString().toFloatOrNull()
            }
        })


        val loadImage = registerForActivityResult(ActivityResultContracts.GetContent()) {
            imageStart.setImageURI(it)
            dtoCreation.imageStart = imageStart.toByteArray()
        }

        sendImageStart.setOnClickListener {
            loadImage.launch("image/*")
        }
    }

    private fun handleDefineLocationView(
        inflate: View,
        inflater: LayoutInflater,
        dtoCreation: CreateOperationDto
    ) {
        spinnerLocations = inflate.findViewById(R.id.actv_define_location)
        spinnerOperations = inflate.findViewById(R.id.actv_define_operation)

        spinnerLocations.adapter =
            ArrayAdapter(
                inflater.context,
                android.R.layout.simple_spinner_item,
                viewModel.locationsOptions
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerLocations.adapter = adapter
            }

        spinnerOperations.adapter = ArrayAdapter(
            inflater.context,
            android.R.layout.simple_spinner_item,
            viewModel.operationsOptions
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerOperations.adapter = adapter
        }

        spinnerLocations.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                dtoCreation.selectedLocation = parent?.getItemAtPosition(0)?.toString()
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                dtoCreation.selectedLocation = parent?.getItemAtPosition(position).toString()
            }
        }

        spinnerOperations.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                dtoCreation.selectedOperation =
                    parent?.getItemAtPosition(0)?.toString()?.let { Operations.fromValue(it) }
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                dtoCreation.selectedOperation =
                    parent?.getItemAtPosition(position)?.toString()?.let { Operations.fromValue(it) }
            }
        }
    }

}