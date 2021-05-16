(function() {
    'use strict';

    angular
        .module('pfeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('sous-famille-depense', {
            parent: 'entity',
            url: '/sous-famille-depense',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'pfeApp.sousFamilleDepense.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/sous-famille-depense/sous-famille-depenses.html',
                    controller: 'SousFamilleDepenseController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('sousFamilleDepense');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('sous-famille-depense-detail', {
            parent: 'sous-famille-depense',
            url: '/sous-famille-depense/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'pfeApp.sousFamilleDepense.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/sous-famille-depense/sous-famille-depense-detail.html',
                    controller: 'SousFamilleDepenseDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('sousFamilleDepense');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'SousFamilleDepense', function($stateParams, SousFamilleDepense) {
                    return SousFamilleDepense.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'sous-famille-depense',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('sous-famille-depense-detail.edit', {
            parent: 'sous-famille-depense-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sous-famille-depense/sous-famille-depense-dialog.html',
                    controller: 'SousFamilleDepenseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SousFamilleDepense', function(SousFamilleDepense) {
                            return SousFamilleDepense.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('sous-famille-depense.new', {
            parent: 'sous-famille-depense',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sous-famille-depense/sous-famille-depense-dialog.html',
                    controller: 'SousFamilleDepenseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                designation: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('sous-famille-depense', null, { reload: 'sous-famille-depense' });
                }, function() {
                    $state.go('sous-famille-depense');
                });
            }]
        })
        .state('sous-famille-depense.edit', {
            parent: 'sous-famille-depense',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sous-famille-depense/sous-famille-depense-dialog.html',
                    controller: 'SousFamilleDepenseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SousFamilleDepense', function(SousFamilleDepense) {
                            return SousFamilleDepense.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('sous-famille-depense', null, { reload: 'sous-famille-depense' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('sous-famille-depense.delete', {
            parent: 'sous-famille-depense',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sous-famille-depense/sous-famille-depense-delete-dialog.html',
                    controller: 'SousFamilleDepenseDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['SousFamilleDepense', function(SousFamilleDepense) {
                            return SousFamilleDepense.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('sous-famille-depense', null, { reload: 'sous-famille-depense' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
