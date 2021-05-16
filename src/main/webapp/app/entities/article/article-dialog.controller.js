(function() {
    'use strict';

    angular
        .module('pfeApp')
        .controller('ArticleDialogController', ArticleDialogController);

    ArticleDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Article', 'Sous_famille'];

    function ArticleDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Article, Sous_famille) {
        var vm = this;

        vm.article = entity;
        vm.clear = clear;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.sous_familles = Sous_famille.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.article.id !== null) {
                Article.update(vm.article, onSaveSuccess, onSaveError);
            } else {
                Article.save(vm.article, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('pfeApp:articleUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        vm.setDocument = function ($file, article) {
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        article.document = base64Data;
                        article.documentContentType = $file.type;
                    });
                });
            }
        };

    }
})();
